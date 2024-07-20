package top.rows.cloud.owl.job.dashboard.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 张治保
 * @since 2024/7/18
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "owl-job.dashboard")
public class DashboardProperties {

    /**
     * 用户名
     */
    private String username = "admin";

    /**
     * 密码
     */
    private String password = "owljob";

    /**
     * 允许跨域访问的地址
     */
    private String[] allowedOrigins;

    /**
     * 用户名密码是否匹配
     *
     * @param name 用户名
     * @param pass 密码
     * @return 是否匹配
     */
    public boolean matched(@NonNull String name, @NonNull String pass) {
        return name.equals(username) && pass.equals(password);
    }

}
