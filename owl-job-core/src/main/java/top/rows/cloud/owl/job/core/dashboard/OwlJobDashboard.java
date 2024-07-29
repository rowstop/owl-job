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
        return errorToString(error, 2);
    }

    /**
     * 异常转 string
     *
     * @param error        异常
     * @param maxStraceDep 异常调用栈的最大长度
     * @return string 格式的异常信息
     */
    public static String errorToString(Throwable error, int maxStraceDep) {
        if (error == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            writeShortenCause(null, error, maxStraceDep, pw);
        }
        return sw.toString();
    }


    private static void writeShortenCause(String caption, Throwable cause, int maxDep, PrintWriter writer) {
        if (caption != null) {
            writer.print(caption);
        }
        writer.println(cause);
        StackTraceElement[] trace = cause.getStackTrace();
        int traceLength = trace.length - 1;
        int curTrace = Math.min(maxDep - 1, traceLength);
        for (int i = 0; i <= curTrace; i++) {
            writer.println("\tat " + trace[i]);
        }
        if (curTrace < traceLength) {
            writer.println("\t... " + (traceLength - curTrace) + "more");
        }
        Throwable causeCause = cause.getCause();
        if (causeCause == null) {
            return;
        }
        writeShortenCause("Caused by: ", causeCause, maxDep, writer);
    }

    /**
     * 懒加载 dashboard 是否存在校验器
     */
    public static class DashboardExistFlag {
        private static final RBucket<Boolean> INSTANCE = redissonClient.getBucket(QueueNames.DASHBOARD_EXITS);
    }

}
