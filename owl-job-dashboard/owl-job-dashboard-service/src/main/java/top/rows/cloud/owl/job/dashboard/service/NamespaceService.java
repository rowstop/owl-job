package top.rows.cloud.owl.job.dashboard.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
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
    Mono<Page<NamespaceVO>> page(Pageable param);
}
