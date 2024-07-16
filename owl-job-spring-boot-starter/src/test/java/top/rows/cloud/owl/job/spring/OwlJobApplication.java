package top.rows.cloud.owl.job.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.rows.cloud.owl.job.api.IOwlJobListener;
import top.rows.cloud.owl.job.api.model.OwlJobParam;

import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/16
 */
@SpringBootApplication
public class OwlJobApplication {

    static final String GROUP = "hello-owl-job";

    public static void main(String[] args) {
        SpringApplication.run(OwlJobApplication.class, args);
    }

    @Bean
    public IOwlJobListener<String> testJobListener() {
        return new IOwlJobListener<String>() {
            @Override
            public String group() {
                return GROUP;
            }

            @Override
            public void run(OwlJobParam<String> param) {
                System.out.println("当前时间：" + LocalDateTime.now());
                System.out.println("设定时间：" + param.getTime());
                System.out.println("读取到的数据" + param);
            }
        };
    }

}
