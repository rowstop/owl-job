##### 📖 中文文档 | [📖 English Documentation](README_EN.md)
# 特性

1. 依赖RedissonClient: 只和redis通信，不额外增加服务端（后面考虑增加监控大屏）
2. 完全依托于客户端，支持动态添加定时任务、支持动态添加任务监听器
3. 支持一次性任务、固定频率任务、cron表达式任务（由[cron-utils](https://github.com/jmrozanec/cron-utils)做表达式解析）

# 1. 使用准备

## 1.1 添加依赖

```xml

<dependency>
    <groupId>top.rows.cloud.owl.job</groupId>
    <artifactId>owl-job-core</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 1.2  其他依赖

### Springboot 项目

```xml

<dependency>
    <groupId>top.rows.cloud.owl.job</groupId>
    <artifactId>owl-job-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```

# 2. 简单使用

[参考](/owl-job-core/src/test/java/top/rows/cloud/owl/job/core)

[SpringBoot参考](/owl-job-spring-boot-starter/src/test/java/top/rows/cloud/owl/job/spring)

## 初始化

```java
    //全局配置
OwlJobConfig timedConfig = new OwlJobConfig()
        .setNamespace("owl-job")
        .setExecutorThreadPool(
                new OwlJobConfig.ThreadPoolProperties()
                        .setThreadNamePrefix("TJ")
                        .setCorePoolSize(50)
                        .setMaxPoolSize(100)
                        .setQueueCapacity(2000)
        );

IOwlJobExecutor executor = new OwlJobExecutor(timedConfig);
IOwlJobTemplate template = new OwlJobTemplate(timedConfig, RedissonClientGetter.get(), executor);
template.init();
```

## 添加任务监听器

```java
    //任务分组
String group = "hello-owl-job";
executor.addListener(group, param->{
    System.out.println("当前时间："+LocalDateTime.now());
    System.out.println("设定时间："+param.getTime());
    System.out.println("读取到的数据"+param);
}）
```

## 添加定时任务

```java
//添加任务
template.add(
        group,
        //设置首次执行时间
        OwlJob.disposable(LocalDateTime.now().plusSeconds(3))
            //设置回调参数
            .setParam("job of disposable")
);
//休眠三秒查看结果
Thread.sleep(3000);
//程序结束 需要终止任务处理
template.shutdown();
```

# 3. 任务监听器注册方式

## 3.1 使用任务执行器 IOwlJobExecutor 进行注册

```java
executor.addListener(
    GROUP,
    (param) ->System.out.println(
            "\n当前时间："+LocalDateTime.now() +
            "\n设定时间："+param.getTime() +
            "\n任务参数："+param.getParam()
    )
);
``` 

或

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

