package top.rows.cloud.owl.job.api;

/**
 * 定时任务监听器
 *
 * @author 张治保
 * @since 2024/6/28
 */
public interface IOwlJobListener extends IOwlJobRunner {

    /**
     * 任务组名称，全局唯一作为任务调度分派依据
     *
     * @return 任务组名
     */
    String group();
}
