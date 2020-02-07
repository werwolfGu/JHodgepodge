# Sentinel

## 限流降级
我们通常以以下3种方式衡量资源的是否处于稳定状态：

- 平均响应时间

    当 1s 内持续进入 5 个请求，对应时刻的平均响应时间（秒级）均超过阈值（count，以 ms 为单位），那么在接下的时间窗口（DegradeRule 中的 timeWindow，以 s 为单位）之内，对这个方法的调用都会自动地熔断（抛出 DegradeException）。注意 Sentinel 默认统计的 RT 上限是 4900 ms，超出此阈值的都会算作 4900 ms，若需要变更此上限可以通过启动配置项 -Dcsp.sentinel.statistic.max.rt=xxx 来配置。
    [RT-GRADE-DEMO](../sentinel-demo/src/main/java/com/gce/demo/degrade/RtDegradeDemo.java)
- 异常比例
    
    当资源的每秒请求量 >= 5，并且每秒异常总数占通过量的比值超过阈值（DegradeRule 中的 count）之后，资源进入降级状态，即在接下的时间窗口（DegradeRule 中的 timeWindow，以 s 为单位）之内，对这个方法的调用都会自动地返回。异常比率的阈值范围是 [0.0, 1.0]，代表 0% - 100%。
    [EXCEPTION-RATIO-DEGRADE-DEMO](../sentinel-demo/src/main/java/com/gce/demo/degrade/ExceptionRatioDegradeDemo.java)
- 异常数量

    当资源近 1 分钟的异常数目超过阈值之后会进行熔断。注意由于统计时间窗口是分钟级别的，若 timeWindow 小于 60s，则结束熔断状态后仍可能再进入熔断状态。
    [EXCEPTION-COUNT-DEGRADE-DEMO](../sentinel-demo/src/main/java/com/gce/demo/degrade/ExceptionCountDegradeDemo.java)

## 限流

当 QPS 超过某个阈值的时候，则采取措施进行流量控制。流量控制的效果包括以下几种：直接拒绝、Warm Up、匀速排队。

一条限流规则主要由下面几个因素组成，我们可以组合这些元素来实现不同的限流效果：

- resource：资源名，即限流规则的作用对象
- count: 限流阈值
- grade: 限流阈值类型（QPS 或并发线程数）
- limitApp: 流控针对的调用来源，若为 default 则不区分调用来源
- strategy: 调用关系限流策略
- controlBehavior: 流量控制效果（直接拒绝、Warm Up、匀速排队）


- QPS限流直接拒绝


    当QPS超过任意规则的阈值后，新的请求就会被立即拒绝，拒绝方式为抛出FlowException。这种方式适用于对系统处理能力确切已知的情况下，
    比如通过压测确定了系统的准确水位时。
    
- Warm Up