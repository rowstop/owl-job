package top.rows.cloud.owl.job.example.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.rows.cloud.owl.job.api.IGroupedOwlTemplate;
import top.rows.cloud.owl.job.api.IOwlJobListener;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;
import top.rows.cloud.owl.job.api.model.IOwlJobParam;
import top.rows.cloud.owl.job.core.GroupedOwlJobTemplate;

import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/25
 */
@Configuration
public class JobConfig {

    private static final String HELLO_WORLD_GROUP = "hello";

    @Bean
    public IGroupedOwlTemplate helloWorldTemplate(IOwlJobTemplate template) {
        return new GroupedOwlJobTemplate(HELLO_WORLD_GROUP, template);
    }

    @Bean
    public IOwlJobListener<String> helloWorldListener() {
        return new IOwlJobListener<String>() {
            @Override
            public String group() {
                return HELLO_WORLD_GROUP;
            }

            @Override
            public void run(IOwlJobParam<String> param) {
                System.out.println("运行时间：" + LocalDateTime.now());
                System.out.println("预定时间：" + param.getTime());
                System.out.println("响应参数：" + param.getParam());
            }
        };
    }

}
