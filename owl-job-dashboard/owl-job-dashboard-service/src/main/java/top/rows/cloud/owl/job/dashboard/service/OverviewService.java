package top.rows.cloud.owl.job.dashboard.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.dashboard.model.dto.OverviewDTO;
import top.rows.cloud.owl.job.dashboard.model.vo.OverviewVO;

/**
 * @author 张治保
 * @since 2024/8/5
 */
public interface OverviewService {

    Flux<String> namespace();

    Mono<OverviewVO> count(OverviewDTO overview);
}
