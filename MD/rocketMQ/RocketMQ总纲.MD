# RocketMQ总纲
## 消息类型

1. 普通消息
   消息队列 RocketMQ 版中无特性的消息，区别于有特性的定时和延时消息、顺序消息和事务消息。
    - 消息发送方式：`同步发送`、`异步发送`、`单向发送` 
2. 事务消息
实现类似 X/Open XA 的分布事务功能，以达到事务最终一致性状态。
3. 顺序消息
允许消息消费者按照消息发送的顺序对消息进行消费。全局顺序和分区顺序
4. 定时延时消息
允许消息生产者对指定消息进行定时（延时）投递，最长支持 40 天。

## 特性功能

- 重置消费位点：根据时间或位点重置消费进度，允许用户进行消息回溯或者丢弃堆积消息。
- 死信队列：将无法正常消费的消息储存到特殊的死信队列供后续处理。
- 集群消费和广播消费：当使用集群消费模式时，消息队列 RocketMQ 版认为任意一条消息只需要被消费者集群内的任意一个消费者处理即可；当使用广播消费模式时，消息队列 RocketMQ 版会将每条消息推送给消费者集群内所有注册过的消费者，保证消息至少被每台机器消费一次。

## 适用场景

- 异步解耦
- 分布式事务的数据一致性
- 消息的顺序收发
    - 全局顺序
    - 分区顺序
- 削峰填谷
- 大规模机器的缓存同步

  使用广播模式实现消息同步
  
## 负载均衡

RocketMQ中的负载均衡都在Client端完成，具体来说的话，主要可以分为Producer端发送消息时候的负载均衡和Consumer端订阅消息的负载均衡。

### Producer的负载均衡
Producer端在发送消息的时候，会先根据Topic找到指定的TopicPublishInfo，在获取了TopicPublishInfo路由信息后，RocketMQ的客户端在默认方式下selectOneMessageQueue()方法会从TopicPublishInfo中的messageQueueList中选择一个队列（MessageQueue）进行发送消息。具体的容错策略均在MQFaultStrategy这个类中定义。这里有一个sendLatencyFaultEnable开关变量，如果开启，在随机递增取模的基础上，再过滤掉not available的Broker代理。所谓的"latencyFaultTolerance"，是指对之前失败的，按一定的时间做退避。例如，如果上次请求的latency超过550Lms，就退避3000Lms；超过1000L，就退避60000L；如果关闭，采用随机递增取模的方式选择一个队列（MessageQueue）来发送消息，latencyFaultTolerance机制是实现消息发送高可用的核心关键所在。

### 客户端负载均衡

1. 平均分配策略
   AllocateMessageQueueAveragely

2.环形分配策略
   AllocateMessageQueueAveragelyByCircle

3.机房分配策略
   AllocateMessageQueueByMachineRoom

4.一致性Hash分配策略

   AllocateMessageQueueConsistentHash

5.配置文件分配策略
   AllocateMessageQueueByConfig


1、Consumer端的心跳包发送

在Consumer启动后，它就会通过定时任务不断地向RocketMQ集群中的所有Broker实例发送心跳包（其中包含了，消息消费分组名称、订阅关系集合、消息通信模式和客户端id的值等信息）。Broker端在收到Consumer的心跳消息后，会将它维护在ConsumerManager的本地缓存变量—consumerTable，同时并将封装后的客户端网络通道信息保存在本地缓存变量—channelInfoTable中，为之后做Consumer端的负载均衡提供可以依据的元数据信息。

2、Consumer端实现负载均衡的核心类—RebalanceImpl

在Consumer实例的启动流程中的启动MQClientInstance实例部分，会完成负载均衡服务线程—RebalanceService的启动（每隔20s执行一次）。通过查看源码可以发现，RebalanceService线程的run()方法最终调用的是RebalanceImpl类的rebalanceByTopic()方法，该方法是实现Consumer端负载均衡的核心。这里，rebalanceByTopic()方法会根据消费者通信类型为“广播模式”还是“集群模式”做不同的逻辑处理。这里主要来看下集群模式下的主要处理流程：

(1) 从rebalanceImpl实例的本地缓存变量—topicSubscribeInfoTable中，获取该Topic主题下的消息消费队列集合（mqSet）；

(2) 根据topic和consumerGroup为参数调用mQClientFactory.findConsumerIdList()方法向Broker端发送获取该消费组下消费者Id列表的RPC通信请求（Broker端基于前面Consumer端上报的心跳包数据而构建的consumerTable做出响应返回，业务请求码：GET_CONSUMER_LIST_BY_GROUP）；

(3) 先对Topic下的消息消费队列、消费者Id排序，然后用消息队列分配策略算法（默认为：消息队列的平均分配算法），计算出待拉取的消息队列。这里的平均分配算法，类似于分页的算法，将所有MessageQueue排好序类似于记录，将所有消费端Consumer排好序类似页数，并求出每一页需要包含的平均size和每个页面记录的范围range，最后遍历整个range而计算出当前Consumer端应该分配到的记录（这里即为：MessageQueue）。

(4) 然后，调用updateProcessQueueTableInRebalance()方法，具体的做法是，先将分配到的消息队列集合（mqSet）与processQueueTable做一个过滤比对。




参考：

[rocketMQ官网](http://rocketmq.apache.org/docs/quick-start/)  
[rocketMQ-github](https://github.com/apache/rocketmq/tree/master/docs/cn)   
[rocketMQ介绍](https://help.aliyun.com/document_detail/155952.html?spm=a2c4g.11186623.6.549.5f227e80znQArh)