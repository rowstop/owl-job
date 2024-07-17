package top.rows.cloud.owl.job.core.model;

/**
 * @author 张治保
 * @since 2024/7/17
 */
public enum OwlJobType {
    //一次性的定时任务
    DISPOSABLE,
    //固定频率速度轮训
    FIX_RATE,
    //cron
    CRON
}
