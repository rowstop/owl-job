package top.rows.cloud.owl.job.core;


import lombok.RequiredArgsConstructor;
import top.rows.cloud.owl.job.api.IGroupedOwlTemplate;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;

/**
 * @author 张治保
 * @since 2024/7/15
 */
@RequiredArgsConstructor
public class GroupedOwlJobTemplate implements IGroupedOwlTemplate {
    /**
     * 任务组名
     */
    private final String group;

    /**
     * 委派 对象
     */
    private final IOwlJobTemplate delegate;

    @Override
    public String group() {
        return group;
    }

    @Override
    public IOwlJobTemplate delegate() {
        return delegate;
    }


}
