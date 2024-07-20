package top.rows.cloud.owl.job.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import top.rows.cloud.owl.job.dashboard.service.NamespaceService;

/**
 * @author 张治保
 * @since 2024/7/20
 */
@RestController
@RequiredArgsConstructor
public class NamespaceController {

    private final NamespaceService namespaceService;
    

}
