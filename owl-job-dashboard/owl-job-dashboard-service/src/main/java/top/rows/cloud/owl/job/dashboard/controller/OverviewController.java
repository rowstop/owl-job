package top.rows.cloud.owl.job.dashboard.controller;

import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.model.dto.OverviewDTO;
import top.rows.cloud.owl.job.dashboard.service.OverviewService;

/**
 * @author 张治保
 * @since 2024/8/5
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/overview")
public class OverviewController {

    private final OverviewService overviewService;

    @PostMapping("/namespace")
    public Mono<SaResult> namespace() {
        return overviewService.namespace()
                .collectList()
                .map(SaResult::data);
    }


    @PostMapping("/count")
    public Mono<SaResult> count(@RequestBody OverviewDTO overview) {
        overview.init();
        return overviewService.count(overview)
                .map(SaResult::data);
    }

}
