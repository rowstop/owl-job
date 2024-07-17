package top.rows.cloud.owl.job.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import top.rows.cloud.owl.job.api.IOwlJobExecutor;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;
import top.rows.cloud.owl.job.api.model.IOwlJobParam;
import top.rows.cloud.owl.job.core.config.OwlJobConfig;
import top.rows.cloud.owl.job.core.model.OwlJob;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 张治保
 * @since 2024/7/16
 */
public class TimedJobTemplateTest {

    private static IOwlJobTemplate template;
    private static IOwlJobExecutor executor;

    @BeforeAll
    static void init() {
        OwlJobConfig timedConfig = new OwlJobConfig()
                .setNamespace("owl-job")
                .setExecutorThreadPool(
                        new OwlJobConfig.ThreadPoolProperties()
                                .setThreadNamePrefix("TJ")
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
        CompletableFuture<IOwlJobParam<Object>> future = new CompletableFuture<>();
        try {
            executor.addListener(group, future::complete);
            template.add(
                    group,
                    //设置首次执行时间
                    OwlJob.disposable(LocalDateTime.now().plusSeconds(3))
                            //设置回调参数
                            .setParam("hello owl")
            );
        } catch (Exception ex) {
            future.completeExceptionally(ex);
        }

        IOwlJobParam<Object> objectTimeJobParam = future.get();
        System.out.println("当前时间：" + LocalDateTime.now());
        System.out.println("设定时间：" + objectTimeJobParam.getTime());
        System.out.println("读取到的数据" + objectTimeJobParam);
    }

    @Test
    void testCron() throws InterruptedException {
        //每分钟的 1,5,9,20,39,48,59秒各支行一次
        String cron = "1,5,9,20,39,48,59 * * * * ?";
        String group = "hello-owl-job-cron";
        CompletableFuture<IOwlJobParam<Object>> future = new CompletableFuture<>();
        try {
            executor.addListener(group, param -> {
                System.out.println("当前时间：" + LocalDateTime.now());
                System.out.println("设定时间：" + param.getTime());
                System.out.println("读取到的数据" + param);
            });
            template.add(
                    group,
                    //设置首次执行时间
                    OwlJob.cron(cron)
                            //设置回调参数
                            .setParam("job of cron")
            );
        } catch (Exception ex) {
            future.completeExceptionally(ex);
        }
        Thread.sleep(10000000);
    }


}
