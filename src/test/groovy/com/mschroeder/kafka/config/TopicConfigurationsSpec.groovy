package com.mschroeder.kafka.config

import spock.lang.Shared
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

class TopicConfigurationsSpec extends Specification {
    @Shared
    Validator validator

    def setupSpec() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator()
    }

    def 'topic name is required'() {
        given:
            TopicConfigurations.TopicConfiguration config = new TopicConfigurations.TopicConfiguration()

        when:
            Set<ConstraintViolation> violations = this.validator.validate(config)

        then:
            ConstraintViolation nameViolation = violations.find { (it.propertyPath.first().name == 'name') }
            nameViolation.message == 'Topic name is required.'
    }
}
