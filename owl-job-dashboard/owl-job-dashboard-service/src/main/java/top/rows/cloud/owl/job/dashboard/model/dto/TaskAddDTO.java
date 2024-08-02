package top.rows.cloud.owl.job.dashboard.model.dto;

import com.cronutils.model.CronType;
import com.cronutils.validation.Cron;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rows.cloud.owl.job.api.model.IOwlJob;
import top.rows.cloud.owl.job.api.model.OwlJobType;
import top.rows.cloud.owl.job.core.model.OwlJob;
import top.rows.cloud.owl.job.dashboard.model.base.GroupKey;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author 张治保
 * @since 2024/7/31
 */
@Getter
@Setter
@ToString
public class TaskAddDTO extends GroupKey {

    /**
     * 任务类型
     */
    @NotNull
    private OwlJobType type;

    /**
     * 任务 id  传了则使用此任务 id 否则默认使用 uuid生成
     */
    private String taskId;

    /**
     * 执行时间 当 tye
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime execTime;

    /**
     * 任务参数 选填
     */
    private String param;

    /**
     * cron 表达式任务
     * tye 为 cron表达式时需要
     */
    @Cron(type = CronType.QUARTZ)
    private String cron;

    /**
     * 固定频率的秒数
     */
    private Long fixedRateSeconds;

    public String taskId() {
        return taskId == null || taskId.isEmpty() ? UUID.randomUUID().toString() : taskId;
    }


    /**
     * 转成 任务 bean
     *
     * @return 任务
     */
    public IOwlJob<String> toJob() {
        OwlJob<String> job;
        switch (type) {
            case CRON: //cron表达式任务
                job = OwlJob.cron(cron);
                break;
            case FIXED_RATE: //固定频率任务
                job = OwlJob.fixedRate(fixedRateSeconds * 1000, execTime);
                break;
            default: //一次性任务
                job = OwlJob.disposable(execTime);
                break;
        }
        if (taskId != null && !taskId.isEmpty()) {
            job.setIdGenerator(() -> taskId);
        }
        return job.setParam(param);
    }

    public void valid() {
        switch (type) {
            case CRON:
                if (cron == null || cron.isEmpty()) {
                    throw new IllegalArgumentException("cron cannot be null");
                }
                break;
            case FIXED_RATE:
                if (fixedRateSeconds == null || fixedRateSeconds < 1) {
                    throw new IllegalArgumentException("fixedRateSeconds cannot be null and fixedRateSeconds must greater than 1");
                }
                if (execTime == null) {
                    throw new IllegalArgumentException("execTime cannot be null");
                }
                break;
            default:
                if (execTime == null) {
                    throw new IllegalArgumentException("execTime cannot be null");
                }
                break;
        }
    }

}
