package top.rows.cloud.owl.job.dashboard.service.impl;

import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.api.OwlJobHelper;
import top.rows.cloud.owl.job.api.model.QueueNames;
import top.rows.cloud.owl.job.core.OwlJobReporter;
import top.rows.cloud.owl.job.dashboard.model.base.Page;
import top.rows.cloud.owl.job.dashboard.model.base.PageParam;
import top.rows.cloud.owl.job.dashboard.model.dto.GroupPageDTO;
import top.rows.cloud.owl.job.dashboard.model.vo.GroupVO;
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
    private final RedissonReactiveClient redissonReactiveClient;

    public NamespaceServiceImpl(RedissonReactiveClient redissonReactiveClient) {
        this.redissonReactiveClient = redissonReactiveClient;
        this.sortedSet = redissonReactiveClient.getScoredSortedSet(QueueNames.NAMESPACE);
    }

    @Override
    public Mono<Page<NamespaceVO>> page(PageParam param) {
        return sortedSet.entryRangeReversed(param.start(), param.end())
                .map(
                        entries -> entries.stream()
                                .map(
                                        entry -> new NamespaceVO()
                                                .setName(entry.getValue())
                                                .setTime(Common.millsToLocalDateTime(entry.getScore().longValue()))
                                ).collect(Collectors.toList())
                )
                .flatMapMany(Flux::fromIterable)
                .flatMap(namespace -> {
                    String name = namespace.getName();
                    return Mono.zip(
                            redissonReactiveClient
                                    .getScoredSortedSet(OwlJobReporter.namespaceGroupKey(name))
                                    .size(),
                            redissonReactiveClient.getMap(OwlJobHelper.confKey(name))
                                    .size()
                    ).map(
                            tuple -> namespace.setGroupCount(tuple.getT1())
                                    .setCurTaskCount(tuple.getT2())
                    );
                })
                .collectList()
                .flatMap(
                        namespaces -> sortedSet.size()
                                .map(total -> Page.of(total, namespaces))
                );
    }

    @Override
    public Mono<Page<GroupVO>> groupPage(GroupPageDTO param) {
        String namespace = param.getNamespace();
        RScoredSortedSetReactive<String> groupSortedSet = redissonReactiveClient.getScoredSortedSet(OwlJobReporter.namespaceGroupKey(namespace));
        return groupSortedSet.valueRange(param.start(), param.end())
                .map(
                        names -> names.stream()
                                .map(name -> new GroupVO().setName(name))
                                .collect(Collectors.toList())
                )
                .flatMap(
                        groups -> groupSortedSet.size()
                                .map(total -> Page.of(total, groups))
                );
    }
}
