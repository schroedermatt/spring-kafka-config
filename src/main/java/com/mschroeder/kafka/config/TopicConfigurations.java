package com.mschroeder.kafka.config;

import lombok.Data;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class TopicConfigurations {
	private List<TopicConfiguration> topics;

	public Optional<List<TopicConfiguration>> getTopics() {
		return Optional.ofNullable(topics);
	}

	@Data
	static class TopicConfiguration {
		@NotNull(message = "Topic name is required.")
		private String name;
		private Integer numPartitions = 3;
		private Short replicationFactor = 1;

		NewTopic toNewTopic() {
			return new NewTopic(this.name, this.numPartitions, this.replicationFactor);
		}
	}
}
