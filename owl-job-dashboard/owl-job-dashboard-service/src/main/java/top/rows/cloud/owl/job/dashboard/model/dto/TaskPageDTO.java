package top.rows.cloud.owl.job.dashboard.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.rows.cloud.owl.job.dashboard.model.base.PageParam;

import javax.validation.constraints.NotBlank;

/**
 * @author 张治保
 * @since 2024/7/30
 */
@Getter
@Setter
@ToString
public class TaskPageDTO extends PageParam {

    @NotBlank
    private String namespace;
}
