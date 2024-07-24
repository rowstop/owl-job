package top.rows.cloud.owl.job.dashboard.service.impl;

import jodd.util.RandomString;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.api.model.QueueNames;
import top.rows.cloud.owl.job.dashboard.model.vo.NamespaceVO;
import top.rows.cloud.owl.job.dashboard.service.NamespaceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 张治保
 * @since 2024/7/20
 */
@Service
public class NamespaceServiceImpl implements NamespaceService {
    private final RScoredSortedSetReactive<String> sortedSet;
    private final List<NamespaceVO> namespaces = new ArrayList<>();

    {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            namespaces.add(
                    new NamespaceVO()
                            .setName(
                                    RandomString.get().randomAlpha(10)
                            ).setTime(
                                    LocalDateTime.now()
                                            .plusMinutes(
                                                    random.nextInt(1000) - 500
                                            )
                            )
            );
        }
    }

    public NamespaceServiceImpl(RedissonClient redissonClient) {
        sortedSet = redissonClient.reactive().getScoredSortedSet(QueueNames.NAMESPACE);
    }

    @Override
    public Mono<Page<NamespaceVO>> page(Pageable param) {
        int number = param.getPageNumber();
        int size = param.getPageSize();
        int startIndex = (number - 1) * size;
        int endIndex = Math.min(startIndex + size, namespaces.size());
        return Mono.just(
                new PageImpl<>(
                        namespaces.subList(startIndex, endIndex),
                        param,
                        namespaces.size()
                )
        );
//        return sortedSet.size()
//                .flatMap(
//                        count -> sortedSet.entryRangeReversed(startIndex, startIndex + size)
//                                .map(entries -> entries.stream()
//                                                .map(
//                                                        entry -> new NamespaceVO()
//                                                                .setName(entry.getValue())
////                                                        .setTime(entry.getScore())
//                                                )
//                                                .collect(Collectors.toList())
//                                ).map(namespaces -> new PageImpl<>(
//                                                namespaces,
//                                                param,
//                                                (long) count
//                                        )
//                                )
//                );
    }
}
