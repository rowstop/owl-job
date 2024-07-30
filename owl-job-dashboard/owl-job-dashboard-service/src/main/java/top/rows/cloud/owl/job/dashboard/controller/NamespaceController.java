package top.rows.cloud.owl.job.dashboard.controller;

import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.model.base.PageParam;
import top.rows.cloud.owl.job.dashboard.model.dto.GroupPageDTO;
import top.rows.cloud.owl.job.dashboard.service.NamespaceService;

import javax.validation.Valid;

/**
 * @author 张治保
 * @since 2024/7/20
 */
@RestController
@RequestMapping("/namespace")
@RequiredArgsConstructor
public class NamespaceController {

    private final NamespaceService namespaceService;

    @PostMapping("/page")
    public Mono<SaResult> page(@RequestBody @Valid PageParam param) {
        return namespaceService.page(param)
                .map(SaResult::data);
    }

    @PostMapping("/group/page")
    public Mono<SaResult> groupPage(@RequestBody @Valid GroupPageDTO param) {
        return namespaceService.groupPage(param)
                .map(SaResult::data);
    }
}
