package top.rows.cloud.owl.job.dashboard.controller;

import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.model.base.PageParam;
import top.rows.cloud.owl.job.dashboard.service.JobLogService;

/**
 * @author 张治保
 * @since 2024/7/26
 */
@RestController
@RequestMapping("/job/log")
@RequiredArgsConstructor
public class JobLogController {

    private final JobLogService jobLogService;

    @PostMapping("/page")
    public Mono<SaResult> page(@RequestBody PageParam param) {
        return jobLogService.page(PageRequest.of(param.getCurrent(), param.getSize()))
                .map(SaResult::data);
    }
}

