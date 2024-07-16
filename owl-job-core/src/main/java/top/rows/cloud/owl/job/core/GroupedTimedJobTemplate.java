package top.rows.cloud.owl.job.core;


import lombok.RequiredArgsConstructor;
import top.rows.cloud.owl.job.api.IGroupedTimedJobTemplate;
import top.rows.cloud.owl.job.api.ITimedJobTemplate;

/**
 * @author 张治保
 * @since 2024/7/15
 */
@RequiredArgsConstructor
public class GroupedTimedJobTemplate implements IGroupedTimedJobTemplate {
    /**
     * 任务组名
     */
    private final String group;

    /**
     * 委派 对象
     */
    private final ITimedJobTemplate delegate;

    @Override
    public String group() {
        return group;
    }

    @Override
    public ITimedJobTemplate delegate() {
        return delegate;
    }


}
