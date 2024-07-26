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
    /**
     * 命名空间名称
     */
    private String name;

    /**
     * 命名空间注册时间
     */
    private LocalDateTime time;
}
