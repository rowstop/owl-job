# 1. 使用准备
## 1.1 添加依赖
```xml
<dependency>
    <groupId>top.rows.cloud.owl.job</groupId>
    <artifactId>owl-job-core</artifactId>
    <version>1.0.0</version>
</dependency>
```
## 1.2  其他依赖
### Springboot 项目
```xml
<dependency>
    <groupId>top.rows.cloud.owl.job</groupId>
    <artifactId>owl-job-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
# 2. 简单使用
<a href="https://github.com/rowstop/owl-job/blob/master/owl-job-core/src/test/java/top/rows/cloud/owl/job/core/TimedJobTemplateTest.java">Examples</a>
## 初始化
```java
    //全局配置
    OwlJobConfig timedConfig = new OwlJobConfig()
        //命名空间
        .setNamespace("owl-job")
        //执行器线程池配置
        .setExecutorThreadPool(
                new OwlJobConfig.ThreadPoolProperties()
                        .setThreadNamePrefix("TJ-")
                        .setCorePoolSize(50)
                        .setMaxPoolSize(100)
                        .setQueueCapacity(2000)
        );
    //初始化任务执行器 （执行器用于添加和执行监听器）
    IOwlJobExecutor executor = new OwlJobExecutor(timedConfig);
    //初始化 template （template 用于添加、移除定时任务）
    IOwlJobTemplate template = new OwlJobTemplate(timedConfig, redissonClient, executor);
    //使用前调用初始化方法
    template.init();
```
## 添加任务监听器
```java
    //任务分组
    String group = "hello-owl-job";
    executor.addListener(group, param->{
        System.out.println("当前时间：" + LocalDateTime.now());
        System.out.println("设定时间：" + param.getTime());
        System.out.println("读取到的数据" + param);
    }）
```

## 添加定时任务
```java
    //添加任务
    template.add(
        group,
        //设置首次执行时间 当前时间加三秒
        OwlJob.of(LocalDateTime.now().plusSeconds(3))
                //设置回调参数
                .setParam("hello owl")
    );
    //休眠三秒查看结果
    Thread.sleep(3000);
    //程序结束 需要终止任务处理
    template.shutdown();
``

