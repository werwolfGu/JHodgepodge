# Sentinel

## 限流降级

熔断降级规则包含下面几个重要的属性：

- resource  资源名，即限流规则的作用对象  
- count 阈值  
- grade 熔断策略，支持秒级 RT/秒级异常比例/分钟级异常数  秒级平均 RT
- timeWindow  降级的时间，单位为 s 
- rtSlowRequestAmount RT 模式下 1 秒内连续多少个请求的平均 RT 超出阈值方可触发熔断（1.7.0 引入） 5
- minRequestAmount  异常熔断的触发最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）  5

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
这个阈值如何判定？
1. 接口是否有调用其他外部接口 若有：外部接口的最大调用量是多少？ 
2. 对有使用到外部资源的如数据库，数据库如链接数也是一个考虑因素；
3. 对于接口本身的处理能力；
4. 一般通过压测来确认接口的阈值来是多少；

重要属性：

- resource： 资源名，即限流规则的作用对象
- count:  限流阈值
- grade:  限流阈值类型（QPS 或并发线程数）
- limitApp: 流控针对的调用来源，若为 default 则不区分调用来源
    
    
    使用limitApp限制调用方时 需要使用 ContextUtil.enter(resourceName, origin) 来记录统计origin调用的次数
    记住  调用完后需要  ContextUtil.exit() ；来退出 否则限流会出现问题；
- strategy: 调用关系限流策略
- controlBehavior: 流量控制效果（直接拒绝、Warm Up、匀速排队） 默认直接拒绝

限流策略
- QPS限流直接拒绝


    当QPS超过任意规则的阈值后，新的请求就会被立即拒绝，拒绝方式为抛出FlowException。这种方式适用于对系统处理能力确切已知的情况下，
    比如通过压测确定了系统的准确水位时。
    
- Warm Up


参考

[sentinel官方文档](https://github.com/alibaba/Sentinel/wiki)