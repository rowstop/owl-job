package top.rows.cloud.owl.job.dashboard.dao.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import top.rows.cloud.owl.job.dashboard.dao.model.User;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
}