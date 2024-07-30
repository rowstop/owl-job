package top.rows.cloud.owl.job.example.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import top.rows.cloud.owl.job.api.IGroupedOwlTemplate;
import top.rows.cloud.owl.job.core.model.OwlJob;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/25
 */
@Configuration
@RequiredArgsConstructor
public class JobAutoInitTest implements InitializingBean, DisposableBean {
    private static final String TASK_ID = "aaaaaa";

    private final IGroupedOwlTemplate helloWorldTemplate;


    @Override
    public void destroy() throws Exception {
        helloWorldTemplate.remove(TASK_ID);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        helloWorldTemplate.add(
                OwlJob.fixedRate(
                        Duration.ofSeconds(10),
                        LocalDateTime.now().plusSeconds(2)
                ).setIdGenerator(
                        () -> TASK_ID
                ).setParam("👋 owl-job")
        );
    }
}
