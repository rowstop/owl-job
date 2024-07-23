package top.rows.cloud.owl.job.dashboard.dao.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author 张治保
 * @since 2024/7/20
 */
@Getter
@Setter
@Accessors(chain = true)
@Table("t_user")
public class User {
    @Id
    private Long id;
    private String username;
}
