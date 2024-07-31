package top.rows.cloud.owl.job.core;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.time.LocalDateTime;

/**
 * @author 张治保
 * @since 2024/7/16
 */
public class RedissonClientGetter {

    static RedissonClient get() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setDatabase(0);
        return Redisson.create(config);
    }

    @Test
    void testLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime parse = LocalDateTime.parse(
                now.toString()
        );
        System.out.println(parse);
        System.out.println(now.equals(parse));
    }
}
