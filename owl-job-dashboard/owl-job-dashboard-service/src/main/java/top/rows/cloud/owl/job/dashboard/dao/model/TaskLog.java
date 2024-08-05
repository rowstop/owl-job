package top.rows.cloud.owl.job.dashboard.dao.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Table;
import top.rows.cloud.owl.job.api.model.OwlJobType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/26
 */
@Getter
@Setter
@Accessors(chain = true)
@Table("owl_task_log")
public class TaskLog extends Base {
    /**
     * 任务所属命名空间
     */
    private String namespace;

    /**
     * 任务分组
     */
    private String taskGroup;

    /**
     * 任务 id
     */
    private String taskId;

    /**
     * 任务类型
     */
    private OwlJobType type;

    /**
     * 任务执行时间
     */
    private LocalDateTime execTime;

    /**
     * 设定时间
     */
    private LocalDateTime settingTime;

    /**
     * 设定时间所属日期
     */
    private LocalDate settingDate;

    /**
     * 任务参数
     */
    private String param;

    /**
     * 异常信息
     */
    private String error;
}
