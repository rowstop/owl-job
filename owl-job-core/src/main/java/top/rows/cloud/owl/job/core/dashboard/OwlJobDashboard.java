package top.rows.cloud.owl.job.core.dashboard;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import top.rows.cloud.owl.job.api.model.IOwlJob;
import top.rows.cloud.owl.job.api.model.QueueNames;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/26
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OwlJobDashboard {

    @Setter
    private static RedissonClient redissonClient;

    /**
     * 上报任务执行结果
     *
     * @param namespace 命名空间
     * @param group     任务分组
     * @param job       任务详情
     * @param error     异常信息 如果没有异常信息代表执行成功 否则执行失败
     */
    public static void reportExecResult(
            String namespace,
            String group,
            String taskId,
            IOwlJob<?> job,
            @Nullable Throwable error
    ) {
        //如果 dashboard 为空 不需要处理
        if (!DashboardExistFlag.INSTANCE.isExists()) {
            return;
        }
        Object param = job.getParam();
        redissonClient.getBlockingDeque(QueueNames.JOB_EXEC_RESULT)
                .offerAsync(
                        new ExecResult()
                                .setNamespace(namespace)
                                .setGroup(group)
                                .setTaskId(taskId)
                                .setExecTime(LocalDateTime.now())
                                .setSettingTime(job.getTime())
                                .setParam(param == null ? null : param.toString())
                                .setError(error == null ? null : errorToString(error))
                );
    }

    /**
     * 异常转 string
     *
     * @param error 异常
     * @return string 格式的异常信息
     */
    public static String errorToString(Throwable error) {
        return errorToString(error, 1500);
    }

    /**
     * 异常转 string
     *
     * @param error     异常
     * @param maxLength 异常字符串的最大长度
     * @return string 格式的异常信息
     */
    public static String errorToString(Throwable error, int maxLength) {
        if (error == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            error.printStackTrace(pw);
        }
        String errorString = sw.toString();
        return errorString.substring(0, Math.min(errorString.length(), maxLength));
    }

    /**
     * 懒加载 dashboard 是否存在校验器
     */
    public static class DashboardExistFlag {
        private static final RBucket<Boolean> INSTANCE = redissonClient.getBucket(QueueNames.DASHBOARD_EXITS);
    }

}
