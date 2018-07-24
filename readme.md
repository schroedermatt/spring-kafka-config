# Spring Kafka Config

This repository holds the configuration for Kafka topics.

### Getting Started

1. Clone the project
2. Run `./gradlew clean build` to run the build and tests

If running a local kafka broker, point the `spring.kafka.bootstrap-servers` prop to your broker
and update the `kafka.topics` in `application.yml` to whatever topics you'd like to create/update.

```yaml
kafka:
  topics:
    -
      name: test-topic-1
      num-partitions: 5
      replication-factor: 1
    -
      name: test-topic-2
      num-partitions: 3
      replication-factor: 1
```

### Adding an Environment

The default `application.yml` will be picked up if no `spring.profiles.active` is declared.

To configure topics in another env, add a new `.yml` file to the resources directory with the
following naming convention -> `application-{env}.yml` (ex. `application-dev.yml`). When
running the application, set `spring.profiles.active` to `{env}` so that those props are picked up.
