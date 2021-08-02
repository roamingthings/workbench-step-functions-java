package de.roamingthings.jokes.fn.status;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.inject.Singleton;

import static org.mockito.Mockito.mock;

@Factory
public class TestBeanFactory {

    @Bean
    @Singleton
    @Primary
    DynamoDbClient dynamoDbClient() {
        return mock(DynamoDbClient.class);
    }
}
