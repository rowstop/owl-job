package top.rows.cloud.owl.job.api.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * @author 张治保
 * @since 2024/6/28
 */

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class OwlJob<T> implements Serializable {

    /**
     * （首次）执行时间
     */
    private LocalDateTime time;

    /**
     * 参数资料
     */
    private T param;

    /**
     * 重复执行周期 单位：纳秒 ，为负则不会重复执行
     * 如果设置了这个值 此任务将在第一次执行后加上这个时间 计算下次执行时间
     */
    private long repeatNanos = 0;

    /**
     * 自定义任务id 生成器
     * 如果为空 系统将使用UUID生成任务 id
     */
    private Supplier<String> idGenerator;

    /**
     * 提供通用的构造方式
     *
     * @param time 任务唯一 id
     *             eg. shopId + "-" +activityId
     *             eg. orderNo
     * @return TimedJobConfig
     */
    public static <T> OwlJob<T> of(@NonNull LocalDateTime time) {
        OwlJob<T> config = new OwlJob<>();
        config.setTime(time);
        return config;
    }

    public void setRepeatDuration(Duration repeatDuration) {
        this.repeatNanos = repeatDuration.toNanos();
    }

    /**
     * 复制一份用于 周期性执行
     *
     * @param next 下次执行时间
     * @return 副本
     */
    public final OwlJob<Object> copyToRepeat(LocalDateTime next) {
        return OwlJob.of(next)
                .setParam(getParam())
                .setRepeatNanos(getRepeatNanos());
    }
}
