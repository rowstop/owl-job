package top.rows.cloud.owl.job.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import top.rows.cloud.owl.job.api.IOwlJobExecutor;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;
import top.rows.cloud.owl.job.api.model.IOwlJobParam;
import top.rows.cloud.owl.job.core.config.OwlJobConfig;
import top.rows.cloud.owl.job.core.model.OwlJob;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
        template.clear();
        template.shutdown();
    }

    /**
     * 一次性定时任务
     *
     * @see OwlJob#disposable(LocalDateTime)
     */
    @Test
    void testDisposable() throws ExecutionException, InterruptedException {
        String group = "disposable";
        CompletableFuture<IOwlJobParam<Object>> future = new CompletableFuture<>();
        try {
            executor.addListener(group, future::complete);
            template.add(
                    group,
                    //设置首次执行时间
                    OwlJob.disposable(LocalDateTime.now().plusSeconds(3))
                            //设置回调参数
                            .setParam("job of disposable")
            );
        } catch (Exception ex) {
            future.completeExceptionally(ex);
        }

        IOwlJobParam<Object> objectTimeJobParam = future.get();
        System.out.println("当前时间：" + LocalDateTime.now());
        System.out.println("设定时间：" + objectTimeJobParam.getTime());
        System.out.println("读取到的数据" + objectTimeJobParam);
    }

    /**
     * @see OwlJob#disposable(long, TimeUnit)
     */
    @Test
    void testDisposable2() throws ExecutionException, InterruptedException {
        String group = "disposable2";
        CompletableFuture<IOwlJobParam<Object>> future = new CompletableFuture<>();
        try {
            executor.addListener(group, future::complete);
            template.add(
                    group,
                    //设置首次执行时间
                    OwlJob.disposable(3, TimeUnit.SECONDS)
                            //设置回调参数
                            .setParam("job of disposable2")
            );
        } catch (Exception ex) {
            future.completeExceptionally(ex);
        }

        IOwlJobParam<Object> objectTimeJobParam = future.get();
        System.out.println("当前时间：" + LocalDateTime.now());
        System.out.println("设定时间：" + objectTimeJobParam.getTime());
        System.out.println("读取到的数据" + objectTimeJobParam);
    }

    /**
     * cron 表达式定时任务
     *
     * @see OwlJob#cron(String)
     */
    @Test
    void testCron() throws InterruptedException {
        //每分钟的 1,5,9,20,39,48,59秒各支行一次
        String cron = "1,5,9,20,39,48,59 * * * * ?";
        String group = "cron";
        executor.addListener(group, param -> {
            System.out.println("当前时间：" + LocalDateTime.now());
            System.out.println("设定时间：" + param.getTime());
            System.out.println("读取到的数据" + param);
        });
        template.add(
                group,
                OwlJob.cron(cron)
                        .setParam("job of cron")
        );
        Thread.sleep(2 * 60 * 1000);
    }

    /**
     * 测试固定频率任务
     *
     * @see OwlJob#fixedRate(long, LocalDateTime)
     */
    @Test
    void testFixedRate() throws InterruptedException {
        //每分钟的 1,5,9,20,39,48,59秒各支行一次
        String group = "fixed-rate";
        executor.addListener(group, param -> {
            System.out.println("当前时间：" + LocalDateTime.now());
            System.out.println("设定时间：" + param.getTime());
            System.out.println("读取到的数据" + param);
        });
        template.add(
                group,
                //首次一秒钟后执行 然后以每 10 秒一次执行任务
                OwlJob.fixedRate(10 * 1000, LocalDateTime.now().plusSeconds(1))
                        //设置回调参数
                        .setParam("job of fixed rate")
        );
        Thread.sleep(2 * 60 * 1000);
    }

    /**
     * 测试固定频率任务
     *
     * @see OwlJob#fixedRate(Duration, LocalDateTime)
     */
    @Test
    void testFixedRate2() throws InterruptedException {
        //每分钟的 1,5,9,20,39,48,59秒各支行一次
        String group = "fixed-rate2";
        executor.addListener(group, param -> {
            System.out.println("当前时间：" + LocalDateTime.now());
            System.out.println("设定时间：" + param.getTime());
            System.out.println("读取到的数据" + param);
        });
        template.add(
                group,
                //首次一秒钟后执行 然后以每 10 秒一次执行任务
                OwlJob.fixedRate(Duration.ofSeconds(10), LocalDateTime.now().plusSeconds(1))
                        //设置回调参数
                        .setParam("job of fixed rate")
        );
        Thread.sleep(2 * 60 * 1000);
    }


}
