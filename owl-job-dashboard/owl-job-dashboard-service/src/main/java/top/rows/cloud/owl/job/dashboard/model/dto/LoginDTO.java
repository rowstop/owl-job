package top.rows.cloud.owl.job.dashboard.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author 张治保
 * @since 2024/7/18
 */
@Getter
@Setter
public class LoginDTO {

    /**
     * 用户名
     */

    @NotBlank
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;

}
