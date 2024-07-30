package top.rows.cloud.owl.job.dashboard.dao.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import top.rows.cloud.owl.job.dashboard.dao.model.TaskLog;

@Repository
public interface TaskLogRepository extends R2dbcRepository<TaskLog, Long> {


//    Mono<Page<JobLog>> page(Example<JobLog> example);
}