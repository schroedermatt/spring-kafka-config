package com.mschroeder.kafka.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class KafkaConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaConfigApplication.class, args);
	}
}
