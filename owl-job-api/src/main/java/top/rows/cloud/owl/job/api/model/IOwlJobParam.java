package top.rows.cloud.owl.job.api.model;

import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/6/28
 */
public interface IOwlJobParam<T> {

    /**
     * 任务参数数据
     */
    T getParam();

    /**
     * 设定的执行时间
     */
    LocalDateTime getTime();

}
