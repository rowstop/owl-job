package top.rows.cloud.owl.job.dashboard.controller;

import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张治保
 * @since 2024/7/30
 */
@RestController
@RequestMapping("/task/manage")
@RequiredArgsConstructor
public class TaskManageController {

    @PostMapping("/page")
    public SaResult page() {
        return SaResult.data(1);
    }
}
