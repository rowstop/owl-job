package top.rows.cloud.owl.job.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import top.rows.cloud.owl.job.api.IOwlJobExecutor;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;
import top.rows.cloud.owl.job.api.model.OwlJobParam;
import top.rows.cloud.owl.job.api.model.OwlJob;
import top.rows.cloud.owl.job.core.config.OwlJobConfig;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 张治保
 * @since 2024/7/16
 */
public class GroupedTimedJobTemplateTest {


    private static IOwlJobTemplate template;
    private static IOwlJobExecutor executor;

    @BeforeAll
    static void init() {
        OwlJobConfig timedConfig = new OwlJobConfig()
                .setNamespace("owl-job")
                .setExecutorThreadPool(
                        new OwlJobConfig.ThreadPoolProperties()
                                .setThreadNamePrefix("TJ-")
                                .setCorePoolSize(50)
                                .setMaxPoolSize(100)
                                .setQueueCapacity(2000)
                );
        executor = new OwlJobExecutor(timedConfig);
        template = new OwlJobTemplate(timedConfig, RedissonClientGetter.get(), executor);
        template.init();
    }

    @AfterAll
    static void shutdown() {
        template.shutdown();
    }

    @Test
    void test() throws ExecutionException, InterruptedException {
        String group = "hello-owl-job";
        GroupedOwlJobTemplate groupedTemplate = new GroupedOwlJobTemplate(group, template);
        CompletableFuture<OwlJobParam<Object>> future = new CompletableFuture<>();
        try {
            executor.addListener(group, future::complete);
            groupedTemplate.add(
                    OwlJob.of(LocalDateTime.now().plusSeconds(3))
                            .setParam("hello owl")
            );
        } catch (Exception ex) {
            future.completeExceptionally(ex);
        }

        OwlJobParam<Object> objectTimeJobParam = future.get();
        System.out.println("当前时间：" + LocalDateTime.now());
        System.out.println("设定时间：" + objectTimeJobParam.getTime());
        System.out.println("读取到的数据" + objectTimeJobParam);

    }
}
