package top.rows.cloud.owl.job.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import top.rows.cloud.owl.job.api.IOwlJobExecutor;
import top.rows.cloud.owl.job.api.model.OwlJob;
import top.rows.cloud.owl.job.core.config.OwlJobConfig;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 张治保
 * @since 2024/7/16
 */
public class TimedJobExecutorTest {

    private static IOwlJobExecutor jobExecutor;

    @BeforeAll
    static void init() {
        jobExecutor = new OwlJobExecutor(new OwlJobConfig());
    }

    @AfterAll
    static void shutdown() {
        if (jobExecutor != null) {
            jobExecutor.shutdown();
        }
    }

    @Test
    void test() throws InterruptedException, ExecutionException {
        String taskGroup = "taskGroup";
        AtomicLong paramBox = new AtomicLong(-1);
        jobExecutor.<Long>addListener(
                taskGroup,
                param -> {
                    System.out.println("数据参数：" + param);
                    paramBox.set(param.getParam());
                }
        );
        String taskId = "2342sfdasdsadt5";
        Long param = 11L;
        jobExecutor.execAsyncFuture(
                null,
                OwlJobHelper.router(taskGroup, taskId),
                OwlJob.of(LocalDateTime.now())
                        .setParam(param)
        ).get();
        Assertions.assertEquals(param, paramBox.get());
    }
}
