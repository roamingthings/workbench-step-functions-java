package de.roamingthings.jokes.fn.status;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Introspected;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.inject.Singleton;

import static org.mockito.Mockito.mock;

@Factory
@Introspected
public class TestBeanFactory {

    @Bean
    @Singleton
    @Primary
    DynamoDbClient dynamoDbClient() {
        return mock(DynamoDbClient.class);
    }

    @Bean
    @Singleton
    @Primary
    JobStatusRepository jobStatusRepository() {
        return mock(JobStatusRepository.class);
    }
}
