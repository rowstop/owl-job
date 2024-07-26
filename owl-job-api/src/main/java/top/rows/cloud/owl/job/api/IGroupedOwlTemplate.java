package top.rows.cloud.owl.job.api;


import top.rows.cloud.owl.job.api.model.IOwlJob;

import java.util.concurrent.CompletionStage;

/**
 * @author 张治保
 * @since 2024/7/15
 */
public interface IGroupedOwlTemplate {


    /**
     * 添加定时任务
     *
     * @param job 定时任务配置
     * @param <T> 定时任务参数
     * @return 任务 id
     */
    default <T> String add(IOwlJob<T> job) {
        return delegate().add(group(), job);
    }

    /**
     * 移除定时任务
     *
     * @param id 任务 id 自行定义的任务 key
     * @return 定时任务配置
     */
    default IOwlJob<Object> remove(String id) {
        return delegate().remove(group(), id);
    }

    /**
     * 异步添加定时任务
     *
     * @param job 定时任务配置
     * @return 异步任务 id
     */
    default <T> CompletionStage<String> addAsync(IOwlJob<T> job) {
        return delegate().addAsync(group(), job);
    }

    /**
     * 异步移除定时任务
     *
     * @param id 任务 id 自行定义的任务 key
     * @return 异步处理结果
     */
    default CompletionStage<IOwlJob<Object>> removeAsync(String id) {
        return delegate().removeAsync(group(), id);
    }


    /**
     * 消息是否已存在
     *
     * @param id 任务 id
     * @return 是否已存在
     */
    default boolean contains(String id) {
        return delegate().contains(group(), id);
    }


    /**
     * 异步判断是否存在
     *
     * @param id 任务 id
     * @return 是否已存在
     */
    default CompletionStage<Boolean> containsAsync(String id) {
        return delegate().containsAsync(group(), id);
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
    IOwlJobTemplate delegate();


}
