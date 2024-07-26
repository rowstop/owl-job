package top.rows.cloud.owl.job.dashboard.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * 分页结果模型
 *
 * @author 张治保
 * @since 2024/7/25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Page<T> implements Serializable {

    /**
     * 总条数
     */
    private long total = 0;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 静态构造方法
     *
     * @param total   总条数
     * @param records 当前页数据
     * @param <T>     每条数据的类型
     * @return 分页结果
     */
    public static <T> Page<T> of(long total, List<T> records) {
        return new Page<T>()
                .setTotal(total)
                .setRecords(records);
    }

    /**
     * Page 的泛型转换
     *
     * @param mapper 转换函数
     * @param <R>    转换后的泛型
     * @return 转换泛型后的 IPage
     */
    @SuppressWarnings("unchecked")
    public <R> Page<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(toList());
        return ((Page<R>) this).setRecords(collect);
    }

}
