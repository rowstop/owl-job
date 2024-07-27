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
import top.rows.cloud.owl.job.dashboard.dao.model.JobLog;
import top.rows.cloud.owl.job.dashboard.dao.repository.JobLogRepository;
import top.rows.cloud.owl.job.dashboard.model.base.Page;
import top.rows.cloud.owl.job.dashboard.service.JobLogService;

/**
 * @author 张治保
 * @since 2024/7/27
 */
@Service
@RequiredArgsConstructor
public class JobLogServiceImpl implements JobLogService {

    private final JobLogRepository jobLogRepository;
    private final R2dbcEntityTemplate r2dbcTemplate;

    @Override
    public Mono<Page<JobLog>> page(Pageable pageable) {
        Example<JobLog> example = Example.of(
                new JobLog(),
                ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
        );
        return jobLogRepository.count(example)
                .flatMap(
                        total -> r2dbcTemplate.select(
                                        Query.query(Criteria.empty())
                                                .with(pageable)
                                                .sort(Sort.by(Sort.Direction.DESC, "execTime")),
                                        JobLog.class
                                ).collectList()
                                .map(records -> Page.of(total, records))
                );
    }
}
