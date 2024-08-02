package top.rows.cloud.owl.job.dashboard.service;

import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.model.base.Page;
import top.rows.cloud.owl.job.dashboard.model.dto.TaskAddDTO;
import top.rows.cloud.owl.job.dashboard.model.dto.TaskKeyDTO;
import top.rows.cloud.owl.job.dashboard.model.dto.TaskPageDTO;
import top.rows.cloud.owl.job.dashboard.model.vo.TaskVO;

/**
 * @author 张治保
 * @since 2024/7/30
 */
public interface TaskService {

    /**
     * 分页查询任务列表
     *
     * @param param 分页参数
     * @return 分页查询结果
     */
    Mono<Page<TaskVO>> page(TaskPageDTO param);

    /**
     * 删除任务
     *
     * @param key 任务 key
     * @return void
     */
    Mono<Void> delete(TaskKeyDTO key);

    /**
     * 新增任务
     *
     * @param task 任务数据
     * @return void
     */
    Mono<Void> add(TaskAddDTO task);
}
