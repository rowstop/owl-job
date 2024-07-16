package top.rows.cloud.owl.job.api;


import lombok.NonNull;
import top.rows.cloud.owl.job.api.model.TimedJob;

import java.util.concurrent.CompletableFuture;

/**
 * 定时任务执行器
 *
 * @author 张治保
 * @since 2024/7/1
 */
public interface ITimedJobExecutor {
    /**
     * 添加任务监听器
     *
     * @param group  任务名称
     * @param runner 任务监听器
     * @return this
     */
    <T> ITimedJobExecutor addListener(@NonNull String group, @NonNull ITimedJobRunner<T> runner);

    /**
     * 添加任务监听器
     *
     * @param listener 任务监听器
     * @return this
     */
    <T> ITimedJobExecutor addListener(@NonNull ITimedJobListener<T> listener);

    /**
     * 删除任务监听起
     *
     * @param group 任务名称
     * @return this
     */
    ITimedJobExecutor removeListener(@NonNull String group);

    /**
     * 同步执行任务
     *
     * @param template template
     * @param router   任务路由
     * @param job      任务配置
     * @param <T>      任务数据类型
     */
    <T> void execAsync(ITimedJobTemplate template, String router, TimedJob<T> job);

    /**
     * 异步执行任务
     *
     * @param template template
     * @param router   任务路由
     * @param job      任务配置
     * @param <T>      任务数据类型
     */
    <T> CompletableFuture<Void> execAsyncFuture(ITimedJobTemplate template, String router, TimedJob<T> job);

    /**
     * 关闭执行器
     */
    void shutdown();
}
