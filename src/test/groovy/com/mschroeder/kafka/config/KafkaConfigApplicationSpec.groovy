package com.mschroeder.kafka.config

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import spock.lang.Specification

@EmbeddedKafka
@SpringBootTest
class KafkaConfigApplicationSpec extends Specification {
	def 'context loads (without any topics configured)'() {
		expect:
			true
	}
}
