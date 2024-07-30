package top.rows.cloud.owl.job.dashboard.service.impl;

import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.api.OwlJobHelper;
import top.rows.cloud.owl.job.api.model.QueueNames;
import top.rows.cloud.owl.job.core.OwlJobReporter;
import top.rows.cloud.owl.job.dashboard.model.base.Page;
import top.rows.cloud.owl.job.dashboard.model.vo.NamespaceVO;
import top.rows.cloud.owl.job.dashboard.service.NamespaceService;
import top.rows.cloud.owl.job.dashboard.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张治保
 * @since 2024/7/20
 */
@Service
public class NamespaceServiceImpl implements NamespaceService {
    private final RScoredSortedSetReactive<String> sortedSet;


    public NamespaceServiceImpl() {
        this.sortedSet = OwlJobReporter.getRedissonClient()
                .reactive()
                .getScoredSortedSet(QueueNames.NAMESPACE);
    }

    @Override
    public Mono<Page<NamespaceVO>> page(Pageable param) {
        int number = param.getPageNumber();
        int size = param.getPageSize();
        int startIndex = (number - 1) * size;
        Mono<List<NamespaceVO>> namespacesMono = sortedSet.entryRangeReversed(startIndex, startIndex + size)
                .map(
                        entries -> {
                            List<NamespaceVO> namespaces = new ArrayList<>();
                            for (ScoredEntry<String> entry : entries) {
                                NamespaceVO namespace = new NamespaceVO()
                                        .setName(entry.getValue())
                                        .setTime(Common.millsToLocalDateTime(entry.getScore().longValue()));
                                namespaces.add(namespace);
                            }
                            return namespaces;
                        }
                );
        return namespacesMono.flatMapIterable(v -> v)
                .flatMap(this::otherData)
                .collectList()
                .flatMap(
                        namespaces -> sortedSet.size()
                                .map(total -> Page.of(total, namespaces))
                );
    }

    private Mono<NamespaceVO> otherData(NamespaceVO namespace) {
        String name = namespace.getName();

        RedissonReactiveClient reactive = OwlJobReporter.getRedissonClient().reactive();
        return Mono.zip(
                reactive
                        .getScoredSortedSet(OwlJobReporter.namespaceGroupKey(name))
                        .size(),
                reactive.getMap(OwlJobHelper.confKey(name))
                        .size()
        ).map(
                tuple -> namespace.setGroupCount(tuple.getT1())
                        .setCurTaskCount(tuple.getT2())
        );

    }
}
