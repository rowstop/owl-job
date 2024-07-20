package top.rows.cloud.owl.job.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author 张治保
 * @since 2024/7/18
 */
@RestController
public class DashboardController {

    @GetMapping("/t")
    public Mono<String> test() {
        return Mono.just("aaaa");
    }
}
