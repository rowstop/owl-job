package top.rows.cloud.owl.job.api;

import lombok.NonNull;

/**
 * @author 张治保
 * @since 2024/7/16
 */
public interface OwlJobHelper {
    /**
     * 任务路由 key 分隔符
     */
    char ROUTER_CONJUNCTION = '\n';

    /**
     * 获取路由 key
     *
     * @param groupName 任务分组名称
     *                  可以做同一个延迟队列的数据隔离 可以把任务数据路由到不同的任务监听器
     * @param taskId    任务 id
     * @return 任务路由 key
     */
    static String router(@NonNull String groupName, @NonNull String taskId) {
        return groupName + ROUTER_CONJUNCTION + taskId;
    }

    /**
     * 从任务路由 key 中取出 任务分组
     *
     * @param router 任务路由
     * @return 任务分组
     */
    static String[] groupAndTaskIdFromRouter(@NonNull String router) {
        int endIndex = router.indexOf(ROUTER_CONJUNCTION);
        return new String[]{
                router.substring(0, endIndex),
                router.substring(endIndex + 1)
        };
    }
}
