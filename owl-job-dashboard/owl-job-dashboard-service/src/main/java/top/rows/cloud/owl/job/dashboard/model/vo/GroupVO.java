package top.rows.cloud.owl.job.dashboard.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 张治保
 * @since 2024/7/30
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class GroupVO implements Serializable {

    private String name;
}
