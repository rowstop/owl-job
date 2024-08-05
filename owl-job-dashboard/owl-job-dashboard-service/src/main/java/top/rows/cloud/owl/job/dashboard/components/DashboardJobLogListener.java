package top.rows.cloud.owl.job.dashboard.components;

import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonShutdownException;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import top.rows.cloud.owl.job.api.model.QueueNames;
import top.rows.cloud.owl.job.core.model.ExecResult;
import top.rows.cloud.owl.job.dashboard.dao.model.TaskLog;
import top.rows.cloud.owl.job.dashboard.dao.repository.TaskLogRepository;

/**
 * @author 张治保
 * @since 2024/7/26
 */
@Slf4j
@Configuration
public class DashboardJobLogListener implements InitializingBean, DisposableBean {

    private final RedissonClient redissonClient;
    private final TaskLogRepository jobLogRepository;
    private final RBlockingDeque<ExecResult> execResultQueue;
    private volatile boolean running;
    private Thread listenerThread;

    public DashboardJobLogListener(RedissonClient redissonClient, TaskLogRepository jobLogRepository) {
        this.redissonClient = redissonClient;
        this.jobLogRepository = jobLogRepository;
        this.execResultQueue = redissonClient.getBlockingDeque(QueueNames.JOB_EXEC_RESULT);
    }

    @Override
    public void afterPropertiesSet() {
        running = true;
        redissonClient.getBucket(QueueNames.DASHBOARD_EXITS).set(Boolean.TRUE);
        listenerThread = new Thread(
                () -> {
                    while (running) {
                        ExecResult result;
                        try {
                            result = execResultQueue.take();
                        } catch (InterruptedException | RedissonShutdownException ex) {
                            if (running) {
                                log.error("任务终止,无法继续取出数据", ex);
                            }
                            return;
                        }
                        jobLogRepository.save(
                                new TaskLog()
                                        .setNamespace(result.getNamespace())
                                        .setTaskGroup(result.getGroup())
                                        .setTaskId(result.getTaskId())
                                        .setType(result.getType())
                                        .setExecTime(result.getExecTime())
                                        .setSettingTime(result.getSettingTime())
                                        .setSettingDate(result.getSettingTime().toLocalDate())
                                        .setParam(result.getParam())
                                        .setError(result.getError())
                        ).block();
                    }
                }
        );
        listenerThread.setDaemon(true);
        listenerThread.setName("ER-Lis");
        listenerThread.start();
    }

    @Override
    public void destroy() {
        running = false;
        redissonClient.getBucket(QueueNames.DASHBOARD_EXITS).delete();
        //中断消费者线程
        if (listenerThread != null) {
            //中断线程
            listenerThread.interrupt();
            try {
                //尽量把当前可能存在的任务执行掉
                listenerThread.join();
            } catch (InterruptedException e) {
                log.error("join interrupted", e);
            }
        }
    }

}
