package top.rows.cloud.owl.job.spring;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import top.rows.cloud.owl.job.api.IOwlJobExecutor;
import top.rows.cloud.owl.job.api.IOwlJobListener;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;
import top.rows.cloud.owl.job.core.OwlJobExecutor;
import top.rows.cloud.owl.job.core.OwlJobTemplate;

import java.util.List;

/**
 * @author 张治保
 * @since 2024/6/28
 */
@ConditionalOnProperty(prefix = "owl-job", name = "enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(name = "org.redisson.api.RedissonClient")
@ConditionalOnBean(type = "org.redisson.api.RedissonClient")
@EnableConfigurationProperties(OwlJobProperties.class)
@RequiredArgsConstructor
public class OwlJobAutoconfigure {

    private final OwlJobProperties timedJobProperties;

    @Bean
    @ConditionalOnMissingBean
    public IOwlJobExecutor timedJobExecutor(@Nullable List<IOwlJobListener<?>> listeners) {
        IOwlJobExecutor executor = new OwlJobExecutor(timedJobProperties.getConfig());
        if (listeners == null || listeners.isEmpty()){
            return executor;
        }
        for (IOwlJobListener<?> listener : listeners) {
            executor.addListener(listener.group(), listener);
        }
        return executor;
    }

    @Bean(initMethod = "init",destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public IOwlJobTemplate timedJobTemplate(RedissonClient redissonClient, IOwlJobExecutor timedJobExecutor) {
        return new OwlJobTemplate(
                timedJobProperties.getConfig(),
                redissonClient,
                timedJobExecutor
        );
    }

}
