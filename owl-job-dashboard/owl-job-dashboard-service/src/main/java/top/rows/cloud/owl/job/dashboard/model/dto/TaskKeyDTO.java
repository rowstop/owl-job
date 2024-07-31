package top.rows.cloud.owl.job.dashboard.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rows.cloud.owl.job.api.OwlJobHelper;

import java.io.Serializable;

/**
 * @author 张治保
 * @since 2024/7/31
 */
@Getter
@Setter
@ToString
public class TaskKeyDTO implements Serializable {

    private String namespace;

    private String group;

    private String taskId;

    public String router() {
        return OwlJobHelper.router(group, taskId);
    }

}
