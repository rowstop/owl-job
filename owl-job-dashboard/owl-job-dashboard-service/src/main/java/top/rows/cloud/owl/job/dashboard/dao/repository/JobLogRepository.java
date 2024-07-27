package top.rows.cloud.owl.job.dashboard.dao.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import top.rows.cloud.owl.job.dashboard.dao.model.JobLog;

@Repository
public interface JobLogRepository extends R2dbcRepository<JobLog, Long> {


//    Mono<Page<JobLog>> page(Example<JobLog> example);
}