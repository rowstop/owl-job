package top.rows.cloud.owl.job.api;


import lombok.NonNull;

import java.util.concurrent.CompletableFuture;

/**
 * 定时任务执行器
 *
 * @author 张治保
 * @since 2024/7/1
 */
public interface IOwlJobExecutor {
    /**
     * 添加任务监听器
     *
     * @param group  任务名称
     * @param runner 任务监听器
     * @return this
     */
    IOwlJobExecutor addListener(@NonNull String group, @NonNull IOwlJobRunner runner);

    /**
     * 添加任务监听器
     *
     * @param listener 任务监听器
     * @return this
     */
    IOwlJobExecutor addListener(@NonNull IOwlJobListener listener);

    /**
     * 删除任务监听起
     *
     * @param group 任务名称
     * @return this
     */
    IOwlJobExecutor removeListener(@NonNull String group);

    /**
     * 同步执行任务
     *
     * @param router   任务路由
     * @param template 定时任务操作模板实例
     */
    void execAsync(IOwlJobTemplate template, String router);

    /**
     * 异步执行任务
     *
     * @param template template
     * @param router   任务路由
     */
    CompletableFuture<Void> execAsyncFuture(IOwlJobTemplate template, String router);

    /**
     * 关闭执行器
     */
    void shutdown();
}
