package top.rows.cloud.owl.job.spring;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.rows.cloud.owl.job.api.IGroupedOwlTemplate;
import top.rows.cloud.owl.job.api.IOwlJobExecutor;
import top.rows.cloud.owl.job.api.IOwlJobListener;
import top.rows.cloud.owl.job.api.IOwlJobTemplate;
import top.rows.cloud.owl.job.api.model.IOwlJobParam;
import top.rows.cloud.owl.job.core.GroupedOwlJobTemplate;
import top.rows.cloud.owl.job.core.model.OwlJob;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 张治保
 * @since 2024/7/16
 */
@SpringBootApplication
public class OwlJobApplication {
    static final String GROUP = "customer-group";
    static final List<LocalDateTime> CALLBACK_TIMES = Collections.synchronizedList(new ArrayList<>());
    static final AtomicReference<Object> CALLBACK_PARAM = new AtomicReference<>();
    static CountDownLatch COUNT_LATCH = new CountDownLatch(100);

    public static void main(String[] args) {
        SpringApplication.run(OwlJobApplication.class, args);
    }

    /**
     * 分组化的template 使用这个 template 只能操作固定的任务分组的任务
     * 不同分组可以注册多个不同 beanName 的 bean
     *
     * @see IGroupedOwlTemplate;
     * @see OwlJob#disposable(long, TimeUnit)
     */
    @Bean
    public IGroupedOwlTemplate customerGroupTemplate(IOwlJobTemplate template) {
        return new GroupedOwlJobTemplate(GROUP, template);
    }

    /**
     * 注册任务监听器 有两种方式注册监听器
     * 1. 直接使用任务执行器的相关 api 注册，这种方式可以动态注册
     * 2. 第二种方式 可以使用 Spring Bean 进行注册
     *
     * @param executor 任务执行器
     * @see OwlJobApplication#testJobListener()
     */
    @Bean
    public ApplicationRunner registerListener(IOwlJobExecutor executor) {
        return args -> executor.addListener(
                GROUP,
                (param) -> {
                    System.out.println(
                            "\n当前时间：" + LocalDateTime.now() +
                                    "\n设定时间：" + param.getTime() +
                                    "\n任务参数：" + param.getParam()
                    );
                    if (COUNT_LATCH.getCount() <= 0) {
                        return;
                    }
                    CALLBACK_TIMES.add(param.getTime());
                    CALLBACK_PARAM.set(param.getParam());
                    COUNT_LATCH.countDown();
                }
        );
    }

    /**
     * 第二种注册方式 使用 SpringBean 注册
     *
     * @see IOwlJobListener
     * @see top.rows.cloud.owl.job.spring.OwlJobAutoconfigure#timedJobExecutor(java.util.List)
     */
//    @Bean
    public IOwlJobListener<String> testJobListener() {
        return new IOwlJobListener<String>() {
            @Override
            public String group() {
                return GROUP;
            }

            @Override
            public void run(IOwlJobParam<String> param) {
                System.out.println("当前时间：" + LocalDateTime.now());
                System.out.println("设定时间：" + param.getTime());
                System.out.println("读取到的数据" + param);
            }
        };
    }


}
