package top.rows.cloud.owl.job.dashboard.controller;

import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.model.dto.TaskKeyDTO;
import top.rows.cloud.owl.job.dashboard.model.dto.TaskPageDTO;
import top.rows.cloud.owl.job.dashboard.service.TaskService;

import javax.validation.Valid;

/**
 * @author 张治保
 * @since 2024/7/30
 */
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/page")
    public Mono<SaResult> page(@RequestBody @Valid TaskPageDTO param) {
        return taskService.page(param)
                .map(SaResult::data);
    }

    @PostMapping("/delete")
    public Mono<SaResult> delete(@RequestBody @Valid TaskKeyDTO key) {
        return taskService.delete(key)
                .map(v -> SaResult.ok());
    }
}
