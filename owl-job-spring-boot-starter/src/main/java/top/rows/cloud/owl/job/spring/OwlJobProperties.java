package top.rows.cloud.owl.job.spring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import top.rows.cloud.owl.job.core.config.OwlJobConfig;

/**
 * @author 张治保
 * @since 2024/7/13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "owl-job")
public class OwlJobProperties {
    /**
     * 是否启用自动装配 定时任务的 bean
     */
    private boolean enable = true;


    @NestedConfigurationProperty
    private OwlJobConfig config = new OwlJobConfig();

}
