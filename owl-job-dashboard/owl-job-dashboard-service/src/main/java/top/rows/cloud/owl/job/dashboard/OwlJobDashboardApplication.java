package top.rows.cloud.owl.job.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.rows.cloud.owl.job.dashboard.properties.DashboardProperties;

/**
 * @author 张治保
 * @since 2024/7/18
 */
@EnableConfigurationProperties(DashboardProperties.class)
@SpringBootApplication
public class OwlJobDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(OwlJobDashboardApplication.class, args);
    }

}
