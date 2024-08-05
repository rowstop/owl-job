package top.rows.cloud.owl.job.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.model.dto.OverviewDTO;
import top.rows.cloud.owl.job.dashboard.model.vo.OverviewVO;
import top.rows.cloud.owl.job.dashboard.service.OverviewService;
import top.rows.cloud.owl.job.dashboard.util.Common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author 张治保
 * @since 2024/8/5
 */
@Service
@RequiredArgsConstructor
public class OverviewServiceImpl implements OverviewService {

    private final R2dbcEntityTemplate r2dbcTemplate;

    @Override
    public Flux<String> namespace() {
        return r2dbcTemplate.getDatabaseClient()
                .sql("SELECT namespace FROM owl_task_log GROUP BY namespace")
                .map((row, metadata) -> row.get("namespace", String.class))
                .all();
    }

    @Override
    public Mono<OverviewVO> count(OverviewDTO overview) {
        String sql = "SELECT setting_date AS settingDate, SUM(IF(error IS NULL,1 ,0)) AS success,SUM(IF(error IS NULL ,0,1)) AS error FROM owl_task_log";
        String namespace = overview.getNamespace();
        boolean where = false;
        if (namespace != null && !namespace.isEmpty()) {
            sql = where(sql, where, "namespace = '" + namespace + "'");
            where = true;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Common.DATE_FORMAT_PATTERN);
        sql = where(
                sql, where,
                "setting_date BETWEEN '" + overview.getStart().format(dateFormatter) +
                        "' AND '" + overview.getEnd().format(dateFormatter) + "'"
        );
        sql += " GROUP BY setting_date ORDER BY settingDate";
        return r2dbcTemplate.getDatabaseClient()
                .sql(sql)
                .map(
                        (row, metadata) -> new OverviewVO.DateCount()
                                .setDate(row.get("settingDate", LocalDate.class))
                                .setSuccess(row.get("success", Long.class))
                                .setError(row.get("error", Long.class))
                ).all()
                .collectList()
                .map(
                        counts -> {
                            long totalSuccess = 0;
                            long totalError = 0;
                            for (OverviewVO.DateCount count : counts) {
                                totalSuccess += count.getSuccess();
                                totalError += count.getError();
                            }
                            return new OverviewVO()
                                    .setSuccess(totalSuccess)
                                    .setError(totalError)
                                    .setCounts(counts);
                        }
                );
    }

    private String where(String sql, boolean where, String condition) {
        sql += (where ? " AND " : " WHERE ") + condition;
        return sql;
    }
}
