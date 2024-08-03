package top.rows.cloud.owl.job.dashboard.constant;

/**
 * @author 张治保
 * @since 2024/7/18
 */
public interface ErrorCodes {

    /**
     * 奇怪的异常
     */
    int STRANGE = 0;

    /**
     * 需要登录
     */
    int NEED_LOGIN = 1;

    /**
     * 用户名密码错误
     */
    int USERNAME_PASSWORD_NOT_MATCHED = 2;

    /**
     * 已在其他地方登录
     */
    int LOGGED_ELSEWHERE = 3;

    /**
     * 错误的 cron 表达式
     */
    int WRONG_CRON = 4;
}
