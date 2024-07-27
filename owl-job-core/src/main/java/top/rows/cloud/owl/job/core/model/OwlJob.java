package top.rows.cloud.owl.job.core.model;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import top.rows.cloud.owl.job.api.model.IOwlJob;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author 张治保
 * @since 2024/6/28
 */


@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class OwlJob<T> implements IOwlJob<T> {

    private static volatile CronParser cronParser;

    /**
     * 执行时间
     */
    @Getter
    private LocalDateTime time;

    /**
     * 任务类型
     */
    private OwlJobType type;

    /**
     * cron 表达式
     */
    private String cron;

    /**
     * 重复执行周期 单位：纳秒 ，为负则不会重复执行
     * 如果设置了这个值 此任务将在第一次执行后加上这个时间 计算下次执行时间
     */
    private long rateNanos = -1;

    /**
     * 参数资料
     */
    @Getter
    @Setter
    private T param;

    /**
     * 自定义任务id 生成器
     * 如果为空 系统将使用UUID生成任务 id
     */
    @Getter
    @Setter
    private Supplier<String> idGenerator;

    /**
     * 当前重试次数
     */
    private int retry;

    /**
     * cron 表达式的定时任务
     *
     * @param quartzCron quartz 格式的 cron 表达式
     * @param <T>        参数类型
     * @return 任务详情
     */
    public static <T> OwlJob<T> cron(@NonNull String quartzCron) {
        OwlJob<T> job = new OwlJob<>();
        job.cron = quartzCron;
        job.type = OwlJobType.CRON;
        LocalDateTime nextTime = nextCronTime(ZonedDateTime.now(), quartzCron);
        if (nextTime == null) {
            throw new RuntimeException("cannot be exec job of cron: " + quartzCron);
        }
        job.time = nextTime;
        return job;
    }

    /**
     * 一次性任务 一当前时间为基准 延迟多少时间执行
     *
     * @param time     延迟多少时间执行
     * @param timeUnit 时间单位
     * @param <T>      参数类型
     * @return IOwlJob
     */
    public static <T> OwlJob<T> disposable(long time, TimeUnit timeUnit) {
        return disposable(LocalDateTime.now().plusNanos(timeUnit.toNanos(time)));
    }

    /**
     * 一次性的任务
     *
     * @param nextTime 下次执行时间
     * @param <T>      参数类型
     * @return IOwlJob
     */
    public static <T> OwlJob<T> disposable(@NonNull LocalDateTime nextTime) {
        OwlJob<T> job = new OwlJob<>();
        job.time = nextTime;
        job.type = OwlJobType.DISPOSABLE;
        return job;
    }

    /**
     * 固定频率执行的任务
     *
     * @param rateDuration 执行频率
     * @param nextTime     下次执行时间
     * @param <T>          参数类型
     * @return IOwlJob
     */
    public static <T> OwlJob<T> fixedRate(Duration rateDuration, @NonNull LocalDateTime nextTime) {
        return fixedRate(rateDuration.toMillis(), nextTime);
    }

    /**
     * 固定频率执行的任务
     *
     * @param rateMills 执行频率 单位毫秒
     * @param nextTime  下次执行时间
     * @param <T>       参数类型
     * @return IOwlJob
     */
    public static <T> OwlJob<T> fixedRate(long rateMills, @NonNull LocalDateTime nextTime) {
        OwlJob<T> job = new OwlJob<>();
        job.time = nextTime;
        job.type = OwlJobType.FIXED_RATE;
        job.rateNanos = rateMills * 1_000_000;
        return job;
    }

    /**
     * 获取下一个执行时间
     *
     * @param quartzCron quartz格式的 cron 表达式
     * @return 下次执行时间
     */
    public static LocalDateTime nextCronTime(ZonedDateTime baseTime, String quartzCron) {
        ExecutionTime executionTime = ExecutionTime.forCron(parser().parse(quartzCron));
        return executionTime
                .nextExecution(baseTime)
                .map(ZonedDateTime::toLocalDateTime)
                .orElse(null);
    }

    /**
     * 懒加载 cron 解析器
     *
     * @return cron 解析器
     */
    private static CronParser parser() {
        if (cronParser != null) {
            return cronParser;
        }
        synchronized (OwlJob.class) {
            if (cronParser != null) {
                return cronParser;
            }
            cronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
        }
        return cronParser;
    }

    @Nullable
    @Override
    public IOwlJob<T> next() {
        //一次性任务
        if (OwlJobType.DISPOSABLE == type) {
            return null;
        }
        //固定频率的任务
        if (OwlJobType.FIXED_RATE == type) {
            return copy(time.plusNanos(rateNanos));
        }
        //cron 表达式任务
        LocalDateTime nextTime = nextCronTime(time.atZone(ZoneId.systemDefault()), cron);
        return nextTime == null ? null : copy(nextTime);
    }

    @Override
    public final int incrementAndGetRetry() {
        return ++retry;
    }

    /**
     * 复制副本
     *
     * @param nextTime 下次执行时间
     * @return 下次执行需要的定时任务数据
     */
    private IOwlJob<T> copy(LocalDateTime nextTime) {
        OwlJob<T> job = new OwlJob<>();
        job.time = nextTime;
        job.type = type;
        job.cron = cron;
        job.rateNanos = rateNanos;
        job.param = param;
        return job;
    }
}
