package top.rows.cloud.owl.job.dashboard.config;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.rows.cloud.owl.job.dashboard.constant.ErrorCodes;

/**
 * [Sa-Token 权限认证] 全局配置类
 */
@Slf4j
@Configuration
public class SaTokenConfigure {
    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 指定 [拦截路由]
                .addInclude("/**")
                // 指定 [放行路由]
                .addExclude("/favicon.ico", "/login", "/test")
                // 指定[认证函数]: 每次请求执行 
                .setAuth(obj -> SaRouter.match("/**", StpUtil::checkLogin))
                // 指定[异常处理函数]：每次[认证函数]发生异常时执行此函数 
                .setError(
                        error -> {
                            if (!(error instanceof SaTokenException)) {
                                log.error("unhandled error", error);
                                return SaResult.code(ErrorCodes.STRANGE);
                            }
                            int code = ((SaTokenException) error).getCode();
                            switch (code) {
                                case 11011:
                                    code = ErrorCodes.NEED_LOGIN;
                                    break;
                                case 11014:
                                    code = ErrorCodes.LOGGED_ELSEWHERE;
                                    break;
                                default:
                                    log.error("unhandled error", error);
                                    code = ErrorCodes.STRANGE;
                                    break;
                            }
                            return SaResult.code(code)
                                    .setMsg(error.getMessage());
                        }
                );
    }
}
