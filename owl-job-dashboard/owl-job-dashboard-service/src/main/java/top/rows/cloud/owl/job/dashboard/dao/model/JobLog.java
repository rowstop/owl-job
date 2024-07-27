package top.rows.cloud.owl.job.dashboard.dao.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/26
 */
@Getter
@Setter
@Accessors(chain = true)
@Table("owl_job_log")
public class JobLog extends Base {
    /**
     * 任务所属命名空间
     */
    private String namespace;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 任务 id
     */
    private String taskId;

    /**
     * 任务执行时间
     */
    private LocalDateTime execTime;

    /**
     * 设定时间
     */
    private LocalDateTime settingTime;

    /**
     * 任务参数
     */
    private String param;

    /**
     * 异常信息
     */
    private String error;
}
