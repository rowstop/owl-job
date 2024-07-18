package top.rows.cloud.owl.job.spring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.rows.cloud.owl.job.api.IGroupedOwlTemplate;
import top.rows.cloud.owl.job.core.model.OwlJob;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static top.rows.cloud.owl.job.spring.OwlJobApplication.*;

/**
 * @author 张治保
 * @since 2024/7/16
 */
@SpringBootTest(classes = OwlJobApplication.class)
public class OwlJobAutoconfigureTest {


    @Autowired
    private IGroupedOwlTemplate customerGroupTemplate;

    void before(int count) {
        CALLBACK_TIMES.clear();
        CALLBACK_PARAM.set(null);
        COUNT_LATCH = new CountDownLatch(count);
    }

    @AfterEach
    void after() {
        customerGroupTemplate.delegate().clear();
    }

    /**
     * 测试一次性任务
     *
     * @see OwlJob#disposable(LocalDateTime)
     */
    @Test
    void testDisposable() throws InterruptedException {
        before(1);
        //执行时间
        LocalDateTime execTime = LocalDateTime.now().plusSeconds(1);
        //执行参数
        String param = "disposable";
        //动态添加任务
        customerGroupTemplate.add(OwlJob.disposable(execTime).setParam(param));
        //等待执行结果
        COUNT_LATCH.await();
        Assertions.assertEquals(execTime, CALLBACK_TIMES.get(0));
        Assertions.assertEquals(param, CALLBACK_PARAM.get());
    }

    /**
     * 测试 一次性任务
     *
     * @see OwlJob#disposable(long, TimeUnit)
     */
    @Test
    void testDisposable2() throws InterruptedException {
        before(1);
        //执行参数
        String param = "disposable2";
        //渲染执行任务
        OwlJob<Object> job = OwlJob.disposable(1, TimeUnit.SECONDS).setParam(param);
        //获取执行时间
        LocalDateTime execTime = job.getTime();
        customerGroupTemplate.add(job);
        //等待所有任务执行完毕
        COUNT_LATCH.await();
        Assertions.assertEquals(execTime, CALLBACK_TIMES.get(0));
        Assertions.assertEquals(param, CALLBACK_PARAM.get());
    }

    /**
     * 测试固定频率任务
     */
    @Test
    void testFixedRate() throws InterruptedException {
        int count = 10;
        before(count);
        //执行参数
        String param = "fixedRate";
        //渲染执行任务
        LocalDateTime firstExecTime = LocalDateTime.now().plusSeconds(1);
        //获取执行时间
        int rateSecond = 1;
        customerGroupTemplate.add(OwlJob.fixedRate(rateSecond * 1000, firstExecTime).setParam(param));
        //等待所有任务执行完毕
        COUNT_LATCH.await();
        Assertions.assertEquals(count, CALLBACK_TIMES.size());
        Assertions.assertEquals(param, CALLBACK_PARAM.get());
        for (int i = 0; i < CALLBACK_TIMES.size(); i++) {
            Assertions.assertEquals(
                    firstExecTime.plusSeconds(i * rateSecond),
                    CALLBACK_TIMES.get(i)
            );
        }

    }

    @Test
    void testCron() throws InterruptedException {
        String cron = "0,10,20,30,40,50 * * * * ?";
        //任务执行次数
        int count = 8;
        before(count);
        //添加任务
        customerGroupTemplate.add(OwlJob.cron(cron));
        //等待所有任务执行完毕
        COUNT_LATCH.await();
        Assertions.assertEquals(count, CALLBACK_TIMES.size());
        int curSecond = CALLBACK_TIMES.get(0).getSecond();
        for (int i = 1; i < CALLBACK_TIMES.size(); i++) {
            Assertions.assertEquals(
                    curSecond = curSecond == 50 ? 0 : curSecond + 10,
                    CALLBACK_TIMES.get(i).getSecond()
            );
        }
    }

}
