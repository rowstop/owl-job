package top.rows.cloud.owl.job.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import top.rows.cloud.owl.job.api.ITimedJobExecutor;
import top.rows.cloud.owl.job.api.ITimedJobTemplate;
import top.rows.cloud.owl.job.api.model.TimeJobParam;
import top.rows.cloud.owl.job.api.model.TimedJob;
import top.rows.cloud.owl.job.core.config.TimedConfig;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 张治保
 * @since 2024/7/16
 */
public class GroupedTimedJobTemplateTest {


    private static ITimedJobTemplate template;
    private static ITimedJobExecutor executor;

    @BeforeAll
    static void init() {
        TimedConfig timedConfig = new TimedConfig()
                .setNamespace("owl-job")
                .setExecutorThreadPool(
                        new TimedConfig.ThreadPoolProperties()
                                .setThreadNamePrefix("TJ-")
                                .setCorePoolSize(50)
                                .setMaxPoolSize(100)
                                .setQueueCapacity(2000)
                );
        executor = new TimedJobExecutor(timedConfig);
        template = new TimedJobTemplate(timedConfig, RedissonClientGetter.get(), executor);
        template.init();
    }

    @AfterAll
    static void shutdown() {
        template.shutdown();
    }

    @Test
    void test() throws ExecutionException, InterruptedException {
        String group = "hello-owl-job";
        GroupedTimedJobTemplate groupedTemplate = new GroupedTimedJobTemplate(group, template);
        CompletableFuture<TimeJobParam<Object>> future = new CompletableFuture<>();
        try {
            executor.addListener(group, future::complete);
            groupedTemplate.add(
                    TimedJob.of(LocalDateTime.now().plusSeconds(3))
                            .setParam("hello owl")
            );
        } catch (Exception ex) {
            future.completeExceptionally(ex);
        }

        TimeJobParam<Object> objectTimeJobParam = future.get();
        System.out.println("当前时间：" + LocalDateTime.now());
        System.out.println("设定时间：" + objectTimeJobParam.getTime());
        System.out.println("读取到的数据" + objectTimeJobParam);

    }
}
