package top.rows.cloud.owl.job.dashboard.config;

import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.rows.cloud.owl.job.dashboard.constant.ErrorCodes;
import top.rows.cloud.owl.job.dashboard.model.base.WrongCronException;

/**
 * @author 张治保
 * @since 2024/8/2
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    /**
     * 错误的cron表达式
     *
     * @return result
     */
    @ExceptionHandler(WrongCronException.class)
    public SaResult wrongCron() {
        return SaResult.code(ErrorCodes.WRONG_CRON)
                .setMsg("wrong cron expression");
    }

    /**
     * 未知异常
     *
     * @param ex 异常
     * @return result
     */
    @ExceptionHandler(Throwable.class)
    public SaResult unknownException(Throwable ex) {
        log.error("unknownException", ex);
        return SaResult.code(ErrorCodes.STRANGE);
    }
}
