package com.mschroeder.kafka.config;

import com.mschroeder.kafka.config.TopicConfigurations.TopicConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * The TopicAdministrator is responsible for pulling in the TopicConfigurations
 * that were configured via the environment's application.yml and looping
 * through the configs to create a NewTopic.class bean in the ApplicationContext
 * so that when the KafkaAdmin.initialize() method fires, the beans are
 * picked up and configured in the cluster.
 */
@Slf4j
@Configuration
public class TopicAdministrator {
	private final TopicConfigurations configurations;
	private final GenericWebApplicationContext context;

	public TopicAdministrator(
			TopicConfigurations configurations,
			GenericWebApplicationContext genericContext) {
		this.configurations = configurations;
		this.context = genericContext;
	}

	/**
	 * Register each TopicConfiguration as a NewTopic.class bean in the Application Context.
	 * The name of each bean will be the provided topic name.
	 */
	@PostConstruct
	public void createTopics() {
		configurations
				.getTopics()
				.ifPresent(this::initializeBeans);
	}

	/**
	 * Loop through TopicConfiguration objects and register them as beans
	 * @param topics TopicConfigurations object
	 */
	private void initializeBeans(List<TopicConfiguration> topics) {
		log.info("Configuring {} topics", topics.size());
		topics.forEach(t -> {
			log.info(
					"topic={},numPartitions={},replicationFactor={}",
					t.getName(),
					t.getNumPartitions(),
					t.getReplicationFactor()
			);
			context.registerBean(t.getName(), NewTopic.class, t::toNewTopic);
		});
	}
}
