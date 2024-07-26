package top.rows.cloud.owl.job.dashboard.service.impl;

import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.api.model.QueueNames;
import top.rows.cloud.owl.job.dashboard.model.base.Page;
import top.rows.cloud.owl.job.dashboard.model.vo.NamespaceVO;
import top.rows.cloud.owl.job.dashboard.service.NamespaceService;
import top.rows.cloud.owl.job.dashboard.util.Common;

import java.util.stream.Collectors;

/**
 * @author 张治保
 * @since 2024/7/20
 */
@Service
public class NamespaceServiceImpl implements NamespaceService {
    private final RScoredSortedSetReactive<String> sortedSet;

    public NamespaceServiceImpl(RedissonClient redissonClient) {
        sortedSet = redissonClient.reactive().getScoredSortedSet(QueueNames.NAMESPACE);
    }

    @Override
    public Mono<Page<NamespaceVO>> page(Pageable param) {
        int number = param.getPageNumber();
        int size = param.getPageSize();
        int startIndex = (number - 1) * size;
        return sortedSet.size()
                .flatMap(
                        count -> sortedSet.entryRangeReversed(startIndex, startIndex + size)
                                .map(entries -> entries.stream()
                                        .map(
                                                entry -> new NamespaceVO()
                                                        .setName(entry.getValue())
                                                        .setTime(Common.millsToLocalDateTime(entry.getScore().longValue()))
                                        )
                                        .collect(Collectors.toList())
                                ).map(namespaces -> Page.of(count, namespaces))
                );
    }
}
