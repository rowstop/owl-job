package top.rows.cloud.owl.job.api.model;

/**
 * @author 张治保
 * @since 2024/7/18
 */
public interface QueueNames {

    String PREFIX = "timed:job";

    String JOB_CONF_PREFIX = PREFIX + ":(inf)";

    String JOB_QUEUE_PREFIX = PREFIX + ":(queue)";
}
