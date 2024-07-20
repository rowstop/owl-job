package top.rows.cloud.owl.job.dashboard.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.rows.cloud.owl.job.dashboard.constant.ErrorCodes;
import top.rows.cloud.owl.job.dashboard.model.dto.LoginDTO;
import top.rows.cloud.owl.job.dashboard.properties.DashboardProperties;

import javax.validation.Valid;

/**
 * @author 张治保
 * @since 2024/7/18
 */
@RequiredArgsConstructor
@RestController
public class SecurityController {

    private final DashboardProperties properties;

    /**
     * 登录页面
     *
     * @param param 请求参数
     * @return 响应结果
     */
    @PostMapping("/login")
    public SaResult login(@RequestBody @Valid LoginDTO param) {
        String username = param.getUsername();
        if (!properties.matched(username, param.getPassword())) {
            return SaResult.code(ErrorCodes.USERNAME_PASSWORD_NOT_MATCHED)
                    .setMsg(" 用户名密码错误");
        }
        StpUtil.login(username);
        return SaResult.data(
                StpUtil.getTokenInfo()
        );
    }


}
