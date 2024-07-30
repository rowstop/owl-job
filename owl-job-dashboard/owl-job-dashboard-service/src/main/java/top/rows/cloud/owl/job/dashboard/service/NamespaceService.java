package top.rows.cloud.owl.job.dashboard.service;

import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.model.base.Page;
import top.rows.cloud.owl.job.dashboard.model.base.PageParam;
import top.rows.cloud.owl.job.dashboard.model.dto.GroupPageDTO;
import top.rows.cloud.owl.job.dashboard.model.vo.GroupVO;
import top.rows.cloud.owl.job.dashboard.model.vo.NamespaceVO;

/**
 * @author 张治保
 * @since 2024/7/20
 */
public interface NamespaceService {

    /**
     * 分页查询 命名空间
     *
     * @param param 分页参数
     * @return 分页查询结果
     */
    Mono<Page<NamespaceVO>> page(PageParam param);

    /**
     * 分页查询任务分组
     *
     * @param param 分页查询参数
     * @return 任务分组分页结果
     */
    Mono<Page<GroupVO>> groupPage(GroupPageDTO param);
}
