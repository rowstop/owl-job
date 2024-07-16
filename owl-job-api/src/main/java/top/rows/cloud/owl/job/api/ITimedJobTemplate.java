package top.rows.cloud.owl.job.api;


import top.rows.cloud.owl.job.api.model.TimedJob;

import java.util.concurrent.CompletionStage;

/**
 * @author 张治保
 * @since 2024/7/1
 */
public interface ITimedJobTemplate {

    /**
     * 添加定时任务
     *
     * @param group 任务分组名称
     * @param job   定时任务配置
     * @param <T>   定时任务参数
     * @return 任务 id
     */
    <T> String add(String group, TimedJob<T> job);

    /**
     * 移除定时任务
     *
     * @param group 任务分组名称
     * @param id    任务 id 自行定义的任务 key
     * @return 定时任务配置
     */
    TimedJob<Object> remove(String group, String id);

    /**
     * 异步添加定时任务
     *
     * @param group 任务分组名称
     * @param job   定时任务配置
     * @return 异步任务 id
     */
    <T> CompletionStage<String> addAsync(String group, TimedJob<T> job);

    /**
     * 异步移除定时任务
     *
     * @param group 任务分组名称
     * @param id    任务 id 自行定义的任务 key
     * @return 异步处理结果
     */
    CompletionStage<TimedJob<Object>> removeAsync(String group, String id);

    /**
     * 开启任务处理
     */
    void init();

    /**
     * 终止任务处理
     */
    void shutdown();

}
