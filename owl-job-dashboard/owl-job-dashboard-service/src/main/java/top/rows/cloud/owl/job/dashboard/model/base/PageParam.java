package top.rows.cloud.owl.job.dashboard.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author 张治保
 * @since 2024/7/22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PageParam {
    /**
     * 当前页码
     */
    private int page = 0;

    /**
     * 当前页数据条数
     */
    private int size = 20;

}
