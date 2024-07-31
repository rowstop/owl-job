package top.rows.cloud.owl.job.dashboard.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.rows.cloud.owl.job.api.model.OwlJobType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/30
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TaskVO implements Serializable {

    private String group;

    private String taskId;

    private OwlJobType type;

    private LocalDateTime nextTime;

    private String param;
}
