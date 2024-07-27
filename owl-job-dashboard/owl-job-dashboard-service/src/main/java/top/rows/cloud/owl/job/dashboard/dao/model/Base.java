package top.rows.cloud.owl.job.dashboard.dao.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author 张治保
 * @since 2024/7/26
 */
@Getter
@Setter
@ToString
public class Base implements Serializable {

    @Id
    private Long id;
}
