package top.rows.cloud.owl.job.core.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 张治保
 * @since 2024/7/1
 */

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class OwlJobConfig {

    /**
     * 定时任务命名空间
     * 不同的命名空间将使用不同的延迟队列 可以做数据隔离
     */
    private String namespace = "timed-job";

    /**
     * 最大失败重试次数 默认三次 小于等于 1表示不重试
     */
    private int retryAttempts = 3;

    /**
     * 重试时间间隔  默认 1s
     * 真实重试时间间隔  重试次数 * retryTimeInterval
     * 第一次次重试 1 * retryTimeInterval
     * 第二次重试   2 * retryTimeInterval
     */
    private Duration retryInterval = Duration.ofSeconds(1);

    /**
     * 任务执行器 线程池配置，触发时使用该线程池执行任务
     */
    private ThreadPoolProperties executorThreadPool = new ThreadPoolProperties();

    /**
     * 用于矫正定时任务执行时间（提前多少毫秒执行定时任务），默认提前 50 毫秒执行
     */
    private long execCorrectionMills = 50;

    /**
     * 获取最大失败重试次数
     *
     * @return 最大失败重试次数 默认三次 小于等于 1表示不重试
     */
    public int getRetryAttempts() {
        return Math.max(retryAttempts, 1);
    }


    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class ThreadPoolProperties implements Serializable {

        /**
         * 线程池线程名前缀
         */
        private String threadNamePrefix = "Future";

        /**
         * 核心线程数
         */
        private int corePoolSize = 1;

        /**
         * 最大线程数
         */
        private int maxPoolSize = 2;

        /**
         * 线程存活时间长度
         */
        private int keepAliveSeconds = 60;

        /**
         * 任务队列长度
         */
        private int queueCapacity = 120;


        public ExecutorService toExecutorService() {
            return new ThreadPoolExecutor(
                    this.getCorePoolSize(),
                    this.getMaxPoolSize(),
                    this.getKeepAliveSeconds(),
                    TimeUnit.SECONDS,
                    createQueue(this.getQueueCapacity()),
                    new ExecurotThreadFactory(this.getThreadNamePrefix()),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );
        }

        /**
         * 线程池队列
         *
         * @param queueCapacity 队列容量
         * @return 线程池队列
         */
        protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
            if (queueCapacity > 0) {
                return new LinkedBlockingQueue<>(queueCapacity);
            } else {
                return new SynchronousQueue<>();
            }
        }

    }


    /**
     * 线程工厂
     */
    public static class ExecurotThreadFactory implements ThreadFactory {
        //线程计数
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        //线程名称前缀
        private final String namePrefix;

        ExecurotThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix + "-";
        }

        public Thread newThread(@NonNull Runnable task) {
            Thread thread = new Thread(task, namePrefix + threadNumber.getAndIncrement());
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }

}
