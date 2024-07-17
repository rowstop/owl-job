package top.rows.cloud.owl.job.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.rows.cloud.owl.job.api.IOwlJobExecutor;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;
import top.rows.cloud.owl.job.api.model.IOwlJobParam;
import top.rows.cloud.owl.job.core.model.OwlJob;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 张治保
 * @since 2024/7/16
 */
@SpringBootTest(classes = OwlJobApplication.class)
public class OwlJobAutoconfigureTest {

    @Autowired
    private IOwlJobExecutor executor;

    @Autowired
    private IOwlJobTemplate template;

    @Test
    public void testDynamicListener() throws ExecutionException, InterruptedException {
        String group = OwlJobApplication.GROUP + "1";
        CompletableFuture<IOwlJobParam<Object>> future = new CompletableFuture<>();
        try {
            executor.addListener(group, future::complete);
            template.add(
                    group,
                    OwlJob.disposable(LocalDateTime.now().plusSeconds(3))
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
    void testAutoListener() throws InterruptedException {
        template.add(
                OwlJobApplication.GROUP,
                OwlJob.disposable(LocalDateTime.now().plusSeconds(3))
                        .setParam("hello owl")
        );
        Thread.sleep(5000);
    }
}
