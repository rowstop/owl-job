package top.rows.cloud.owl.job.dashboard.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.rows.cloud.owl.job.api.model.OwlJobType;
import top.rows.cloud.owl.job.dashboard.dao.model.TaskLog;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/31
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TaskLogVO {
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
     * 任务参数
     */
    private String param;

    /**
     * 时间误差 单位 ms
     */
    private long timeError;

    /**
     * 异常信息
     */
    private String error;

    public static TaskLogVO of(TaskLog log) {
        return new TaskLogVO()
                .setNamespace(log.getNamespace())
                .setTaskGroup(log.getTaskGroup())
                .setTaskId(log.getTaskId())
                .setType(log.getType())
                .setExecTime(log.getExecTime())
                .setSettingTime(log.getSettingTime())
                .setTimeError(Duration.between(log.getExecTime(), log.getSettingTime()).toMillis())
                .setParam(log.getParam())
                .setError(log.getError());

    }

}
