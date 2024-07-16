package top.rows.cloud.owl.job.api;

/**
 * 定时任务监听器
 *
 * @author 张治保
 * @since 2024/6/28
 */
public interface ITimedJobListener<T> extends ITimedJobRunner<T> {

    /**
     * 任务组名称，全局唯一作为任务调度分派依据
     *
     * @return 任务组名
     */
    String group();
}
