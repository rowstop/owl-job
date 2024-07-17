package top.rows.cloud.owl.job.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import top.rows.cloud.owl.job.api.IOwlJobExecutor;
import top.rows.cloud.owl.job.api.IOwlJobListener;
import top.rows.cloud.owl.job.api.IOwlJobRunner;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;
import top.rows.cloud.owl.job.api.model.IOwlJob;
import top.rows.cloud.owl.job.core.config.OwlJobConfig;
import top.rows.cloud.owl.job.core.model.OwlJobParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * 定时任务执行器
 *
 * @author 张治保
 * @since 2024/6/29
 */
@Slf4j
public class OwlJobExecutor implements IOwlJobExecutor {
    /**
     * 执行任务的线程池
     */
    private final ExecutorService executor;
    /**
     * 最大失败重试次数
     */
    private final int maxFailRetry;

    /**
     * 重试时间间隔
     */
    private final long retryInterval;

    /**
     * 任务组及对应监听器 map
     */
    private final Map<String, IOwlJobRunner<?>> groupListenerMap = new HashMap<>();

    public OwlJobExecutor(OwlJobConfig config) {
        this.executor = config.getExecutorThreadPool().toExecutorService();
        this.maxFailRetry = config.getRetryAttempts();
        this.retryInterval = config.getRetryInterval().toMillis();
    }

    @Override
    public final <T> OwlJobExecutor addListener(@NonNull String group, @NonNull IOwlJobRunner<T> runner) {
        groupListenerMap.put(group, runner);
        return this;
    }

    @Override
    public final <T> IOwlJobExecutor addListener(@NonNull IOwlJobListener<T> listener) {
        groupListenerMap.put(listener.group(), listener);
        return this;
    }

    @Override
    public final OwlJobExecutor removeListener(@NonNull String group) {
        groupListenerMap.remove(group);
        return this;
    }

    /**
     * 执行任务
     *
     * @param template 任务管理器
     * @param job      任务配置
     * @param <T>      任务数据类型
     */
    @Override
    public final <T> void execAsync(IOwlJobTemplate template, String router, IOwlJob<T> job) {
        executor.execute(() -> execTask(false, template, router, job));
    }

    @Override
    public <T> CompletableFuture<Void> execAsyncFuture(IOwlJobTemplate template, String router, IOwlJob<T> job) {
        return CompletableFuture.runAsync(
                () -> execTask(false, template, router, job),
                executor
        );
    }

    @Override
    public void shutdown() {
        if (executor == null || executor.isShutdown()) {
            return;
        }
        executor.shutdown();
    }

    private <T> void execTask(boolean skipRepeat, IOwlJobTemplate template, String router, IOwlJob<T> job) {
        String[] groupAndTaskId = OwlJobHelper.groupAndTaskIdFromRouter(router);
        String group = groupAndTaskId[0];
        IOwlJobRunner<?> runner = groupListenerMap.get(group);
        if (runner == null) {
            log.warn("done not have job runner for group:{}", group);
            return;
        }
        // 执行定时任务
        //增加重试操作
        // 当前重试次数
        int retry = 0;
        // 重试间隔
        while (retry < maxFailRetry) {
            try {
                run(runner, job);
                break;
            } catch (Exception e) {
                retry++;
                log.error("重试次数：{}", retry);
                log.error("定时任务执行失败,任务组名：{},任务数据：{}", group, job);
                log.error("失败原因", e);
                try {
                    Thread.sleep(retry * retryInterval);
                } catch (InterruptedException interruptedException) {
                    log.error("重试异常", interruptedException);
                }
            }
        }
        //是否需要重复执行 下个执行周期不为空 即需要继续执行任务
        IOwlJob<T> nextJob;
        if (skipRepeat || (nextJob = job.next()) == null) {
            return;
        }
        //异步执行
        executor.execute(() -> {
            IOwlJob<T> next = nextJob;
            //需不需要 立即执行 
            //当满足 当前时间大于等于下次执行时间时表示需要立即执行
            //立即执行将在本地循环异步执行  
            while (next != null && !LocalDateTime.now().isBefore(next.getTime())) {
                IOwlJob<T> executingJob = next;
                executor.execute(() -> execTask(true, template, router, executingJob));
                next = next.next();
            }
            //如果下次执行数据为空 则表示不需要继续执行
            if (next == null) {
                return;
            }
            //当前时间小于执行时间 放到延迟队列里 延迟执行 
            template.addAsync(group, next.setIdGenerator(() -> groupAndTaskId[1]));
        });
    }

    /**
     * 执行任务
     *
     * @param runner 任务执行器
     * @param job    任务详情
     * @param <T>    人数参数类型
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> void run(IOwlJobRunner<?> runner, IOwlJob<T> job) {
        runner.run(
                new OwlJobParam()
                        .setTime(job.getTime())
                        .setParam(job.getParam())
        );
    }


}
