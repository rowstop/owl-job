package top.rows.cloud.owl.job.api.model;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * @param <T> 任务参数类型
 * @author 张治保
 * @since 2024/6/28
 */
public interface IOwlJob<T extends Serializable> extends Serializable {
    /**
     * 获取当前任务的参数
     *
     * @return 任务参数
     */
    @Nullable
    T getParam();

    /**
     * 获取当前任务的执行时间
     *
     * @return 任务执行时间
     */
    LocalDateTime getTime();

    /**
     * 获取任务类型
     *
     * @return 任务类型
     */
    OwlJobType getType();

    /**
     * 获取 自定义的任务 id 生成器 如果为空则默认使用 UUID 生成
     *
     * @return 自定义的任务 id 生成器
     */
    @Nullable
    Supplier<String> getIdGenerator();

    /**
     * 设置 id 生成器
     *
     * @param idGenerator id 生成器
     */
    IOwlJob<T> setIdGenerator(Supplier<String> idGenerator);

    /**
     * 获取下次执行任务数据
     */
    @Nullable
    IOwlJob<T> next();

    /**
     * 把重试次数+1 并返回
     */
    int incrementAndGetRetry();


}
