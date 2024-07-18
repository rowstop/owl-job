# Features

1. Depends on RedissonClient: communicates only with Redis, without additional server overhead (considering adding a
   monitoring dashboard later).
2. Completely client-based, supports dynamically adding scheduled tasks and task listeners.
3. Supports one-time tasks, fixed-rate tasks, and cron expression tasks (
   using [cron-utils](https://github.com/jmrozanec/cron-utils) for expression parsing).

# 1. Preparation for Use

## 1.1 Add Dependencies

```xml

<dependency>
    <groupId>top.rows.cloud.owl.job</groupId>
    <artifactId>owl-job-core</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 1.2 Other Dependencies

### Spring Boot Projects

```xml

<dependency>
    <groupId>top.rows.cloud.owl.job</groupId>
    <artifactId>owl-job-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```

# 2. Simple Usage

[Reference](/owl-job-core/src/test/java/top/rows/cloud/owl/job/core)
[Spring Boot Reference](/owl-job-spring-boot-starter/src/test/java/top/rows/cloud/owl/job/spring)

## Initialization

```java
// Global configuration
OwlJobConfig timedConfig = new OwlJobConfig()
        .setNamespace("owl-job") // Namespace
        .setExecutorThreadPool(new OwlJobConfig.ThreadPoolProperties()
                .setThreadNamePrefix("TJ-")
                .setCorePoolSize(50)
                .setMaxPoolSize(100)
                .setQueueCapacity(2000)
        );

// Initialize job executor (used for adding and executing listeners)
IOwlJobExecutor executor = new OwlJobExecutor(timedConfig);

// Initialize template (used for adding and removing scheduled tasks)
IOwlJobTemplate template = new OwlJobTemplate(timedConfig, redissonClient, executor);

// Call initialization method before use
template.

init();
```

## Add Task Listeners

```java
// Task group
String group = "hello-owl-job";

executor.

addListener(group, param ->{
        System.out.

println("Current time: "+LocalDateTime.now());
        System.out.

println("Scheduled time: "+param.getTime());
        System.out.

println("Data read: "+param);
});
```

## Add Scheduled Task

```java
// Add task
template.add(group,
             OwlJob.of(LocalDateTime.now().

plusSeconds(3)) // Set initial execution time (current time plus three seconds)
        .

setParam("hello owl") // Set callback parameter
);

// Sleep for three seconds to see the result
        Thread.

Thread.sleep(3000);

// End program; shutdown task processing
template.

shutdown();
```

# 3. Task Listener Registration Methods

## 3.1 Register using IOwlJobExecutor

```java
 executor.addListener(
        GROUP,
        (param) ->System.out.

println(
                "\n当前时间："+LocalDateTime.now() +
        "\n设定时间："+param.

getTime() +
        "\n任务参数："+param.

getParam()
        )
                )
```

OR

```java
executor.addListener(
        new IOwlJobListener<Object>() {
    @Override
    public String group () {
        return GROUP;
    }

    @Override
    public void run (IOwlJobParam < Object > param) {
        System.out.println(
                "\n当前时间：" + LocalDateTime.now() +
                        "\n设定时间：" + param.getTime() +
                        "\n任务参数：" + param.getParam()
        );
    }
}
);
```
