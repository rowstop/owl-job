package top.rows.cloud.owl.job.dashboard.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author 张治保
 * @since 2024/7/22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PageParam implements Serializable {
    /**
     * 当前页码
     */
    @Min(1)
    private int current = 1;

    /**
     * 当前页数据条数
     */
    @Min(1)
    @Max(200)
    private int size = 20;


    public int start() {
        return (current - 1) * size;
    }


    public int end() {
        return current * size;
    }


}
