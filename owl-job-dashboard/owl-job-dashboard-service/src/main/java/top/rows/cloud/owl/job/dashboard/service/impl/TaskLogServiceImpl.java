package top.rows.cloud.owl.job.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.dao.model.TaskLog;
import top.rows.cloud.owl.job.dashboard.dao.repository.TaskLogRepository;
import top.rows.cloud.owl.job.dashboard.model.base.Page;
import top.rows.cloud.owl.job.dashboard.service.TaskLogService;

/**
 * @author 张治保
 * @since 2024/7/27
 */
@Service
@RequiredArgsConstructor
public class TaskLogServiceImpl implements TaskLogService {

    private final TaskLogRepository jobLogRepository;
    private final R2dbcEntityTemplate r2dbcTemplate;

    @Override
    public Mono<Page<TaskLog>> page(Pageable pageable) {
        Example<TaskLog> example = Example.of(
                new TaskLog(),
                ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
        );
        return jobLogRepository.count(example)
                .flatMap(
                        total -> r2dbcTemplate.select(
                                        Query.query(Criteria.empty())
                                                .with(pageable)
                                                .sort(Sort.by(Sort.Direction.DESC, "execTime")),
                                        TaskLog.class
                                ).collectList()
                                .map(records -> Page.of(total, records))
                );
    }
}
