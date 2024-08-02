package top.rows.cloud.owl.job.dashboard.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 张治保
 * @since 2024/7/31
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class GroupKey implements Serializable {

    @NotBlank
    private String namespace;

    @NotBlank
    private String group;

}
