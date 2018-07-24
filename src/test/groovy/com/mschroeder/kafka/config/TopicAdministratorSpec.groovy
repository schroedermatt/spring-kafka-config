package com.mschroeder.kafka.config

import com.mschroeder.kafka.config.util.ApplicationContextUtil
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@DirtiesContext
@EmbeddedKafka
@SpringBootTest
@ActiveProfiles("topics")
class TopicAdministratorSpec extends Specification {
	@Autowired
	TopicConfigurations configurations

	@Autowired
	ApplicationContextUtil contextUtil

	@Autowired
	AdminClient kafkaClient

	@Autowired
	KafkaAdmin kafkaAdmin

	def 'topics are registered as beans and created in embedded environment'() {
		given:
			Collection<String> embeddedTopics = kafkaClient
					.listTopics()
					.names()
					.get()

		expect:
			configurations
					.getTopics()
					.ifPresent { topics ->
						topics.each {
							assert contextUtil.context.isBeanNameInUse(it.name)
							assert embeddedTopics.contains(it.name)
							assert kafkaClient.getPartitionCount(it.name) == it.numPartitions
						}
					}
	}

	def 'topics partitions are updated when increasing the partition count'() {
		given: 'an updated NewTopic bean'
			def existingTopic = configurations.topics.get().first()
			def updatedConfig = new TopicConfigurations.TopicConfiguration(
					name: existingTopic.name,
					numPartitions: existingTopic.numPartitions + 5,
					replicationFactor: existingTopic.replicationFactor
			)

			contextUtil.updateBean(existingTopic.name, NewTopic, updatedConfig.toNewTopic())

		when: 'initializing the NewTopic beans'
			kafkaAdmin.initialize()

		then: 'the partition count should be updated'
			kafkaClient.getPartitionCount(existingTopic.name) == updatedConfig.numPartitions
	}

	def 'topics partitions are not updated when decreasing the partition count'() {
		given: 'an updated NewTopic bean'
			def existingTopic = configurations.topics.get().last()
			def updatedConfig = new TopicConfigurations.TopicConfiguration(
					name: existingTopic.name,
					numPartitions: existingTopic.numPartitions - 5,
					replicationFactor: existingTopic.replicationFactor
			)

			contextUtil.updateBean(existingTopic.name, NewTopic, updatedConfig.toNewTopic())

		when: 'initializing the NewTopic beans'
			kafkaAdmin.initialize()

		then: 'partition count is not updated'
			kafkaClient.getPartitionCount(existingTopic.name) == existingTopic.numPartitions
	}
}