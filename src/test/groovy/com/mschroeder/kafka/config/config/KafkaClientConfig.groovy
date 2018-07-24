package com.mschroeder.kafka.config.config

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.common.KafkaFuture
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaClientConfig {
    @Value('${spring.embedded.kafka.brokers}')
    private String brokers

    @Bean
    AdminClient kafkaClient() {
        def client = AdminClient.create([
                (AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG): brokers
        ])

        // append a helper method for retrieving a topic's partition count
        client.metaClass.getPartitionCount = { String topicName ->
            AdminClient thisClient = delegate as AdminClient
            Integer count = null

            Map<String, KafkaFuture> topicDescriptions = thisClient
                    .describeTopics([topicName])
                    .values()

            if (!topicDescriptions.isEmpty() && topicDescriptions[topicName] != null) {
                count = topicDescriptions[topicName]
                        .get()
                        .partitions()
                        .size()
            }

            return count
        }

        return client
    }
}
