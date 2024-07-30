package top.rows.cloud.owl.job.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.rows.cloud.owl.job.api.model.OwlJobType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/26
 */

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ExecResult implements Serializable {

    private String namespace;

    private String group;

    private String taskId;

    private OwlJobType type;

    private LocalDateTime execTime;

    private LocalDateTime settingTime;

    private String param;

    private String error;

}
