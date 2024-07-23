package top.rows.cloud.owl.job.dashboard.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/20
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class NamespaceVO {
    private String name;
    private LocalDateTime time;
}
