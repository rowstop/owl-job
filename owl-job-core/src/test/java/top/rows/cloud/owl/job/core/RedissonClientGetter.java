package top.rows.cloud.owl.job.core;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

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
}
