package top.rows.cloud.owl.job.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonShutdownException;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import top.rows.cloud.owl.job.api.IOwlJobExecutor;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;
import top.rows.cloud.owl.job.api.model.IOwlJob;
import top.rows.cloud.owl.job.core.config.OwlJobConfig;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author 张治保
 * @since 2024/6/29
 */
@Slf4j
public class OwlJobTemplate implements IOwlJobTemplate {

    /**
     * 队列缓存 key前缀
     */
    private final static String QUEUE_NAME = "timed:job:";
    /**
     * blockingQueue 阻塞队列
     * delayedQueue  延迟队列
     * jobExecutor 任务执行器
     * jobConfigRMap redisson 任务配置map 用于获取任务数据
     */
    private final RedissonClient redissonClient;
    private final RBlockingQueue<String> blockingQueue;
    private final RDelayedQueue<String> delayedQueue;
    private final IOwlJobExecutor jobExecutor;
    private final RMap<String, IOwlJob<Object>> jobConfigRMap;
    private final long execCorrectionMills;

    /**
     * 任务消费者线程
     */
    private Thread consumerThread;
    /**
     * 用于标记是否处于运行中
     */
    private volatile boolean running;

    /**
     * 构造方法 初始化 redisson 阻塞队列 和 延迟队列
     *
     * @param redissonClient redisson 客户端
     * @param executor       定时任务执行器
     */
    public OwlJobTemplate(OwlJobConfig config, RedissonClient redissonClient, IOwlJobExecutor executor) {
        //获取全局命名空间
        @NonNull String namespace = config.getNamespace();
        this.redissonClient = redissonClient;
        this.jobConfigRMap = redissonClient.getMap(QUEUE_NAME + "(inf):" + namespace);
        this.blockingQueue = redissonClient.getBlockingQueue(QUEUE_NAME + "(queue):" + namespace);
        this.delayedQueue = redissonClient.getDelayedQueue(this.blockingQueue);
        this.jobExecutor = executor;
        this.execCorrectionMills = config.getExecCorrectionMills();
    }

    private static <T> String taskId(@NonNull IOwlJob<T> job) {
        Supplier<String> idGenerator = job.getIdGenerator();
        if (idGenerator == null) {
            return UUID.randomUUID().toString();
        }
        //置为空 避免被序列化
        job.setIdGenerator(null);
        return idGenerator.get();
    }

    /**
     * 执行时间转为 延迟执行毫秒
     *
     * @param time 执行时间
     * @return 延迟执行毫秒
     */
    private long toDelayMills(LocalDateTime time) {
        Duration duration = Duration.between(LocalDateTime.now(), time);
        //最多提前 70 毫秒执行 矫正执行时间
        long delay = duration.toMillis() - execCorrectionMills;
        return Math.max(delay, 0);
    }

    /**
     * 添加定时任务
     *
     * @param job 定时任务配置
     * @param <T> 定时任务参数
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <T> String add(@NonNull String group, @NonNull IOwlJob<T> job) {
        String taskId = taskId(job);
        String router = OwlJobHelper.router(group, taskId);
        jobConfigRMap.put(router, (IOwlJob<Object>) job);
        delayedQueue.offer(
                router,
                toDelayMills(job.getTime()),
                TimeUnit.MILLISECONDS
        );
        return taskId;
    }

    /**
     * 移除定时任务
     *
     * @param id 任务id
     */
    @Override
    public final IOwlJob<Object> remove(@NonNull String group, @NonNull String id) {
        String router = OwlJobHelper.router(group, id);
        delayedQueue.remove(router);
        return jobConfigRMap.remove(router);
    }

    /**
     * 异步添加定时任务
     *
     * @param job 定时任务配置
     * @param <T> 定时任务参数
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <T> CompletionStage<String> addAsync(@NonNull String group, @NonNull IOwlJob<T> job) {
        String taskId = taskId(job);
        String router = OwlJobHelper.router(group, taskId);
        return jobConfigRMap.putAsync(router, (IOwlJob<Object>) job)
                .thenApply(
                        (_pre) -> {
                            delayedQueue.offer(
                                    router,
                                    toDelayMills(job.getTime()),
                                    TimeUnit.MILLISECONDS
                            );
                            return taskId;
                        }
                );
    }

    /**
     * 异步移除定时任务
     *
     * @param id 任务 id
     * @return 异步处理结果
     */
    @Override
    public final CompletionStage<IOwlJob<Object>> removeAsync(@NonNull String group, @NonNull String id) {
        String router = OwlJobHelper.router(group, id);
        return delayedQueue.removeAsync(router)
                .thenApply((_v) -> jobConfigRMap.remove(router));
    }

    /**
     * 初始化方法
     */
    @Override
    public final void init() {
        if (running) {
            return;
        }
        //标记为运行中
        running = true;
        //初始化消费者线程（常驻线程） 
        consumerThread = new Thread(() -> {
            //循环获取定时任务 并交由任务执行器执行
            while (running) {
                String router;
                try {
                    router = blockingQueue.take();
                } catch (InterruptedException | RedissonShutdownException ex) {
                    if (running) {
                        log.error("线程终止,无法继续取出数据", ex);
                    }
                    return;
                }
                //已获取到任务
                //获取任务详情并删除原数据
                IOwlJob<Object> job = jobConfigRMap.remove(router);
                if (job == null) {
                    continue;
                }
                jobExecutor.execAsync(this, router, job);
            }
        });
        consumerThread.setDaemon(true);
        consumerThread.setName("Timed-Job");
        consumerThread.start();
    }

    /**
     * 程序退出方法
     */
    @Override
    public final void shutdown() {
        //标记为停止运行
        running = false;
        //关闭 redisson client
        if (!redissonClient.isShutdown() && !redissonClient.isShuttingDown()) {
            try {
                this.redissonClient.shutdown();
            } catch (Exception ignore) {
            }
        }
        //中断消费者线程
        if (consumerThread != null) {
            //中断线程
            consumerThread.interrupt();
            try {
                //尽量把当前可能存在的任务执行掉
                consumerThread.join();
            } catch (InterruptedException e) {
                log.error("join interrupted", e);
            }
        }
        //关闭 executor
        if (jobExecutor != null) {
            jobExecutor.shutdown();
        }
    }


}
