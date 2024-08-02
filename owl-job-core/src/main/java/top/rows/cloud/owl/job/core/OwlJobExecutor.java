package top.rows.cloud.owl.job.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import top.rows.cloud.owl.job.api.*;
import top.rows.cloud.owl.job.api.model.IOwlJob;
import top.rows.cloud.owl.job.core.config.OwlJobConfig;
import top.rows.cloud.owl.job.core.model.OwlJobParam;

import java.io.Serializable;
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
     * 所属命名空间
     */
    private final String namespace;
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
    private final Map<String, IOwlJobRunner> groupListenerMap = new HashMap<>();

    /**
     * 用以标记程序是否正在运行中
     */
    private volatile boolean running = true;

    public OwlJobExecutor(OwlJobConfig config) {
        this.namespace = config.getNamespace();
        this.executor = config.getExecutorThreadPool().toExecutorService();
        this.maxFailRetry = config.getRetryAttempts();
        this.retryInterval = config.getRetryInterval().toMillis();
    }

    @Override
    public final OwlJobExecutor addListener(@NonNull String group, @NonNull IOwlJobRunner runner) {
        if (!groupListenerMap.containsKey(group)) {
            OwlJobReporter.groupReport(namespace, group);
        }
        groupListenerMap.put(group, runner);
        return this;
    }

    @Override
    public final IOwlJobExecutor addListener(@NonNull IOwlJobListener listener) {
        return this.addListener(listener.group(), listener);
    }

    @Override
    public final OwlJobExecutor removeListener(@NonNull String group) {
        groupListenerMap.remove(group);
        return this;
    }

    @Override
    public void execAsync(IOwlJobTemplate template, String router) {
        executor.execute(() -> execTask(template, router));
    }

    @Override
    public CompletableFuture<Void> execAsyncFuture(IOwlJobTemplate template, String router) {
        return CompletableFuture.runAsync(
                () -> execTask(template, router),
                executor
        );
    }

    @Override
    public void shutdown() {
        if (!running) {
            return;
        }
        running = false;
        OwlJobReporter.removeGroupReport(namespace, groupListenerMap.keySet());
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    private void execTask(IOwlJobTemplate template, String router) {
        IOwlJob<String> job = template.getJobConfig(router);
        if (job == null) {
            return;
        }
        String[] groupAndTaskId = OwlJobHelper.groupAndTaskIdFromRouter(router);
        String group = groupAndTaskId[0];
        IOwlJobRunner runner = groupListenerMap.get(group);
        if (runner == null) {
            log.warn("done not have job runner for group:{}", group);
            return;
        }
        String taskId = groupAndTaskId[1];
        //执行任务 如果失败则尝试重试
        //失败不需要继续执行 已重新提交至任务队列
        if (runRetry(template, runner, taskId, group, job)) {
            return;
        }
        //是否需要重复执行 下个执行周期不为空 即需要继续执行任务
        IOwlJob<String> nextJob = job.next();
        if (nextJob == null) {
            //不需要执行则删除原配置
            template.removeJobConfig(router);
            return;
        }
        //需要重复执行 则直接存放值任务队列
        template.addAsync(group, nextJob.setIdGenerator(() -> groupAndTaskId[1]));
    }

    /**
     * 执行任务 并实现失败重试
     * 1. 如果任务执行失败 并且未超过最大重试次数 返回 true
     * 2. 否则（任务执行成功 或已超过最大重试次数） 返回 false
     *
     * @return 任务是否已重新存放至任务队列
     */

    private boolean runRetry(
            IOwlJobTemplate template,
            IOwlJobRunner runner,
            String group,
            String taskId,
            IOwlJob<String> job) {
        // 执行定时任务
        //增加重试操作
        // 当前重试次数
        // 重试间隔
        Throwable error = null;
        LocalDateTime execTime = LocalDateTime.now();
        try {
            run(runner, job);
        } catch (Throwable e) {
            error = e;
        }
        //上报执行结果
        OwlJobReporter.reportExecResult(namespace, group, taskId, job, execTime, error);
        int curRetry;
        //如果没有异常 或重试次数大于等于最大重试次数  则不需要继续处理
        if (error == null || (curRetry = job.incrementAndGetRetry()) >= maxFailRetry) {
            return false;
        }

        //以下为异常重试逻辑
        log.error("重试次数：{}", curRetry);
        log.error("定时任务执行失败,任务组名：{},任务数据：{}", group, job);
        log.error("失败原因", error);
        //休眠一定时间后 重新添加到任务队列
        try {
            Thread.sleep(curRetry * retryInterval);
        } catch (InterruptedException interruptedException) {
            log.error("重试异常", interruptedException);
        }
        template.addAsync(group, job.setIdGenerator(() -> taskId));
        return true;
    }

    /**
     * 执行任务
     *
     * @param runner 任务执行器
     * @param job    任务详情
     * @param <T>    人数参数类型
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T extends Serializable> void run(IOwlJobRunner runner, IOwlJob<T> job) {
        runner.run(
                new OwlJobParam()
                        .setTime(job.getTime())
                        .setParam(job.getParam())
        );
    }


}
