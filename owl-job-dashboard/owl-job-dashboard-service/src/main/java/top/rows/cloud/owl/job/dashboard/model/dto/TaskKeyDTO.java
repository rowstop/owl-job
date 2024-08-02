package top.rows.cloud.owl.job.dashboard.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rows.cloud.owl.job.api.OwlJobHelper;
import top.rows.cloud.owl.job.dashboard.model.base.GroupKey;

import javax.validation.constraints.NotBlank;

/**
 * @author 张治保
 * @since 2024/7/31
 */
@Getter
@Setter
@ToString
public class TaskKeyDTO extends GroupKey {

    @NotBlank
    private String taskId;

    public String router() {
        return OwlJobHelper.router(getGroup(), taskId);
    }

}
