package top.rows.cloud.owl.job.api;


import top.rows.cloud.owl.job.api.model.TimeJobParam;

/**
 * @author 张治保
 * @since 2024/7/11
 */
@FunctionalInterface
public interface ITimedJobRunner<T> {
    /**
     * 执行定时任务
     *
     * @param param 任务参数
     */
    void run(TimeJobParam<T> param);
}
