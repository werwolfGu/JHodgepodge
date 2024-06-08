package com.gce.producter;

import org.apache.rocketmq.common.admin.ConsumeStats;
import org.apache.rocketmq.common.admin.OffsetWrapper;
import org.apache.rocketmq.common.admin.TopicOffset;
import org.apache.rocketmq.common.admin.TopicStatsTable;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

import java.util.HashMap;

/**
 * @Author chengen.gce
 * @DATE 2023/5/17 22:28
 */
public class RocketMQExtDemo {

    public static void main(String[] args) {
        DefaultMQAdminExt admin = new DefaultMQAdminExt();
        admin.setNamesrvAddr("localhost:9876");
        try {
            admin.start();
            // 获取消费者群组的连接信息
            String consumerGroup = "consumerGroup";
            TopicStatsTable topicStatsTable =admin.examineTopicStats(consumerGroup);
            HashMap<MessageQueue, TopicOffset> map = topicStatsTable.getOffsetTable();
            for (MessageQueue mq : map.keySet()) {
                TopicOffset topicOffset = map.get(mq);
                System.out.println("topicOffset = " + topicOffset);
                System.out.println("clientId:" + mq.getQueueId());
            }


            ConsumeStats stats = admin.examineConsumeStats(consumerGroup);
            HashMap<MessageQueue, OffsetWrapper> statMap = stats.getOffsetTable();
            for (MessageQueue mq : statMap.keySet()) {
                OffsetWrapper offsetWrapper = statMap.get(mq);
                System.out.println("offsetWrapper = " + offsetWrapper);
                System.out.println("clientId:" + mq.getQueueId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            admin.shutdown();
        }

    }
}
