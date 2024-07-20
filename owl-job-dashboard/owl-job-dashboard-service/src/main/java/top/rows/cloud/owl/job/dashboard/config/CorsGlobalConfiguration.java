package top.rows.cloud.owl.job.dashboard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import top.rows.cloud.owl.job.dashboard.properties.DashboardProperties;

@Configuration
@RequiredArgsConstructor
public class CorsGlobalConfiguration implements WebFluxConfigurer {

    private final DashboardProperties dashboardProperties;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry corsRegistry) {
        String[] allowedOrigins = dashboardProperties.getAllowedOrigins();
        if (allowedOrigins == null || allowedOrigins.length == 0) {
            return;
        }
        corsRegistry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("POST")
                .maxAge(3600);
    }
}