package top.rows.cloud.owl.job.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import org.redisson.RedissonObject;
import org.redisson.api.RMapReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.rows.cloud.owl.job.api.OwlJobHelper;
import top.rows.cloud.owl.job.api.model.IOwlJob;
import top.rows.cloud.owl.job.core.OwlJobReporter;
import top.rows.cloud.owl.job.core.OwlJobTemplate;
import top.rows.cloud.owl.job.dashboard.model.base.Page;
import top.rows.cloud.owl.job.dashboard.model.dto.TaskAddDTO;
import top.rows.cloud.owl.job.dashboard.model.dto.TaskKeyDTO;
import top.rows.cloud.owl.job.dashboard.model.dto.TaskPageDTO;
import top.rows.cloud.owl.job.dashboard.model.vo.TaskVO;
import top.rows.cloud.owl.job.dashboard.service.TaskService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张治保
 * @since 2024/7/30
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final RedissonReactiveClient redissonReactiveClient;

    private RMapReactive<String, IOwlJob<String>> taskConfMap(String namespace) {
        return redissonReactiveClient.getMap(OwlJobHelper.confKey(namespace), OwlJobReporter.CODEC);
    }

    private RScoredSortedSetReactive<String> delayQueue(String namespace) {
        return redissonReactiveClient.getScoredSortedSet(
                RedissonObject.prefixName("redisson_delay_queue_timeout", OwlJobHelper.blockingQueueKey(namespace))
        );
    }

    @Override
    public Mono<Page<TaskVO>> page(TaskPageDTO param) {
        String namespace = param.getNamespace();
        RMapReactive<String, IOwlJob<String>> taskConfMap = taskConfMap(namespace);

        return taskConfMap.readAllKeySet()
                .flatMap(
                        keySet -> {
                            ArrayList<String> keys = new ArrayList<>(keySet);
                            int total = keys.size();
                            List<String> curConfKeys = keys.subList(param.start(), Math.min(param.end(), total));
                            return taskConfMap.getAll(new HashSet<>(curConfKeys))
                                    .map(
                                            confMap -> {
                                                List<TaskVO> records = confMap.entrySet()
                                                        .stream()
                                                        .map(entry -> {
                                                            String key = entry.getKey();
                                                            IOwlJob<String> jobConf = entry.getValue();
                                                            String[] groupTaskId = OwlJobHelper.groupAndTaskIdFromRouter(key);
                                                            return new TaskVO()
                                                                    .setGroup(groupTaskId[0])
                                                                    .setTaskId(groupTaskId[1])
                                                                    .setType(jobConf.getType())
                                                                    .setNextTime(jobConf.getTime())
                                                                    .setParam(jobConf.getParam());
                                                        })
                                                        .collect(Collectors.toList());
                                                return Page.of(total, records);
                                            }
                                    );

                        }
                );

    }

    @Override
    public Mono<Void> delete(TaskKeyDTO key) {
        String namespace = key.getNamespace();
        String router = key.router();
        return taskConfMap(namespace)
                .remove(router)
                .then(delayQueue(namespace).remove(router))
                .then();
    }

    @Override
    public Mono<Void> add(TaskAddDTO task) {
        OwlJobTemplate owlJobTemplate = new OwlJobTemplate(
                task.getNamespace(),
                50,
                null
        );
        return Mono.fromCompletionStage(owlJobTemplate.addAsync(task.getGroup(), task.toJob()))
                .then();
    }
}
