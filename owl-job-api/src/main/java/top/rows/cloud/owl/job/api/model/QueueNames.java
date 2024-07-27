package top.rows.cloud.owl.job.api.model;

/**
 * @author 张治保
 * @since 2024/7/18
 */
public interface QueueNames {

    String PREFIX = "owl:job";

    String CONF_PREFIX = PREFIX + ":(inf)";

    String QUEUE_PREFIX = PREFIX + ":(queue)";

    String NAMESPACE = PREFIX + ":(namespace)";

    String DASHBOARD_EXITS = PREFIX + ":(dashboard)";

    String JOB_EXEC_RESULT = PREFIX + ":(exec:result)";
}
