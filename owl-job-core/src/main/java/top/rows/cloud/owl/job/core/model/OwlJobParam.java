package top.rows.cloud.owl.job.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.rows.cloud.owl.job.api.model.IOwlJobParam;

import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/6/28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class OwlJobParam<T> implements IOwlJobParam<T> {

    /**
     * 任务参数数据
     */
    private T param;

    /**
     * 设定的执行时间
     */
    private LocalDateTime time;

}
