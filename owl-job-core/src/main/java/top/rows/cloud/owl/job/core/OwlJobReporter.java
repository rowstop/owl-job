package top.rows.cloud.owl.job.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.MarshallingCodec;
import top.rows.cloud.owl.job.api.model.IOwlJob;
import top.rows.cloud.owl.job.api.model.QueueNames;
import top.rows.cloud.owl.job.core.model.ExecResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 仅当 dashboard 存在时生效
 *
 * @author 张治保
 * @since 2024/7/26
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OwlJobReporter {

    public static final Codec CODEC = new MarshallingCodec();
    @Getter
    private static RedissonClient redissonClient;

//    static {
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(
//                new SimpleModule()
//                        .addSerializer(LocalDateTime.class, top.rows.cloud.owl.job.core.sd.LocalDateTime.SERIALIZER)
//                        .addDeserializer(LocalDateTime.class, top.rows.cloud.owl.job.core.sd.LocalDateTime.DESERIALIZER)
//        );
//        CODEC = new JsonJacksonCodec(mapper);
//    }

    public static void namespaceReport(String namespace) {
        report(true, QueueNames.NAMESPACE, Collections.singleton(namespace));
    }

    public static RScoredSortedSet<String> getReportSet(String key) {
        return redissonClient.getScoredSortedSet(key);
    }

    public static void removeNamespaceReport(Set<String> namespace) {
        report(false, QueueNames.NAMESPACE, namespace);
    }

    public static void groupReport(String namespace, String group) {
        report(true, namespaceGroupKey(namespace), Collections.singleton(group));
    }

    public static String namespaceGroupKey(String namespace) {
        return QueueNames.NAMESPACE_GROUP + ":" + namespace;
    }

    public static void removeGroupReport(String namespace, Set<String> groups) {

        report(false, namespaceGroupKey(namespace), groups);
    }

    private static void report(boolean add, String key, Collection<String> vals) {
        if (add) {
            long score = System.currentTimeMillis();
            getReportSet(key).addAll(
                    vals.stream()
                            .collect(
                                    Collectors.toMap(v -> v, (v) -> (double) score)
                            )
            );
            return;
        }
        getReportSet(key).removeAll(vals);
    }

    /**
     * 上报任务执行结果
     *
     * @param namespace 命名空间
     * @param group     任务分组
     * @param job       任务详情
     * @param execTime  任务执行时间
     * @param error     异常信息 如果没有异常信息代表执行成功 否则执行失败
     */
    public static void reportExecResult(
            String namespace,
            String group,
            String taskId,
            IOwlJob<?> job,
            LocalDateTime execTime,
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
                                .setType(job.getType())
                                .setExecTime(execTime)
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

    public static void setRedissonClient(RedissonClient redissonClient) {
        if (OwlJobReporter.redissonClient == null) {
            OwlJobReporter.redissonClient = redissonClient;
        }
    }

    /**
     * 懒加载 dashboard 是否存在校验器
     */
    public static class DashboardExistFlag {
        private static final RBucket<Boolean> INSTANCE = redissonClient.getBucket(QueueNames.DASHBOARD_EXITS);
    }

}
