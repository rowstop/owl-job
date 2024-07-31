package top.rows.cloud.owl.job.dashboard.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.model.base.Page;
import top.rows.cloud.owl.job.dashboard.model.vo.TaskLogVO;


/**
 * @author 张治保
 * @since 2024/7/27
 */
public interface TaskLogService {

    /**
     * 分页查询任务日志
     *
     * @param pageable 分页查询参数
     * @return 分页查询结果
     */
    Mono<Page<TaskLogVO>> page(Pageable pageable);
}
