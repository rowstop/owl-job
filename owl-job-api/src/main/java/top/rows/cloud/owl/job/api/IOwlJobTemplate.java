package top.rows.cloud.owl.job.api;


import top.rows.cloud.owl.job.api.model.IOwlJob;

import java.util.concurrent.CompletionStage;

/**
 * 任务操作模板抽象类
 *
 * @author 张治保
 * @since 2024/7/1
 */
public interface IOwlJobTemplate {

    /**
     * 添加定时任务
     *
     * @param group 任务分组名称
     * @param job   定时任务配置
     * @return 任务 id
     */
    String add(String group, IOwlJob<String> job);

    /**
     * 异步添加定时任务
     *
     * @param group 任务分组名称
     * @param job   定时任务配置
     * @return 异步任务 id
     */
    CompletionStage<String> addAsync(String group, IOwlJob<String> job);

    /**
     * 移除定时任务
     *
     * @param group 任务分组名称
     * @param id    任务 id 自行定义的任务 key
     * @return 定时任务配置
     */
    IOwlJob<String> remove(String group, String id);

    /**
     * 异步移除定时任务
     *
     * @param group 任务分组名称
     * @param id    任务 id 自行定义的任务 key
     * @return 异步处理结果
     */
    CompletionStage<IOwlJob<String>> removeAsync(String group, String id);

    /**
     * 消息是否已存在
     *
     * @param group 任务分组
     * @param id    任务 id
     * @return 是否已存在
     */
    boolean contains(String group, String id);

    /**
     * 异步判断是否存在
     *
     * @param group 任务分组
     * @param id    任务 id
     * @return 是否已存在
     */
    CompletionStage<Boolean> containsAsync(String group, String id);


    /**
     * 直接获取任务详情
     *
     * @param group 任务分组组名
     * @param id    任务 id
     * @return 任务详情
     */
    default IOwlJob<String> getJobConfig(String group, String id) {
        return getJobConfig(OwlJobHelper.router(group, id));
    }

    /**
     * 直接获取任务详情
     *
     * @param router 任务路由
     * @return 任务详情
     */
    IOwlJob<String> getJobConfig(String router);


    /**
     * 近删除任务详情 不删除定时任务
     *
     * @param group 任务分组组名
     * @param id    任务 id
     */
    default void removeJobConfig(String group, String id) {
        removeJobConfig(OwlJobHelper.router(group, id));
    }


    /**
     * 近删除任务详情 不删除定时任务
     *
     * @param router 任务路由
     */
    void removeJobConfig(String router);

    /**
     * 清空所有任务
     */
    void clear();

    /**
     * 异步清空所有任务
     */
    CompletionStage<Void> clearAsync();

    /**
     * 开启任务处理
     */
    void init();

    /**
     * 终止任务处理
     */
    void shutdown();

}
