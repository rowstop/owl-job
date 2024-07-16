package top.rows.cloud.owl.job.api;


import top.rows.cloud.owl.job.api.model.TimedJob;

import java.util.concurrent.CompletionStage;

/**
 * @author 张治保
 * @since 2024/7/15
 */
public interface IGroupedTimedJobTemplate {


    /**
     * 添加定时任务
     *
     * @param job 定时任务配置
     * @param <T> 定时任务参数
     * @return 任务 id
     */
    default <T> String add(TimedJob<T> job) {
        return delegate().add(group(), job);
    }

    /**
     * 移除定时任务
     *
     * @param id 任务 id 自行定义的任务 key
     * @return 定时任务配置
     */
    default TimedJob<Object> remove(String id) {
        return delegate().remove(group(), id);
    }

    /**
     * 异步添加定时任务
     *
     * @param job 定时任务配置
     * @return 异步任务 id
     */
    default <T> CompletionStage<String> addAsync(TimedJob<T> job) {
        return delegate().addAsync(group(), job);
    }

    /**
     * 异步移除定时任务
     *
     * @param id 任务 id 自行定义的任务 key
     * @return 异步处理结果
     */
    default CompletionStage<TimedJob<Object>> removeAsync(String id) {
        return delegate().removeAsync(group(), id);
    }

    /**
     * 任务分组组名
     *
     * @return 任务分组组名
     */
    String group();

    /**
     * 委派
     */
    ITimedJobTemplate delegate();
    

}
