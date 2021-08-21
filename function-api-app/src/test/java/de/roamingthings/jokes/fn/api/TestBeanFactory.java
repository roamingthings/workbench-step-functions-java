package de.roamingthings.jokes.fn.api;

import de.roamingthings.jokes.fn.api.createjob.ReferenceNumberGenerator;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sfn.SfnClient;

import javax.inject.Singleton;
import javax.ws.rs.core.SecurityContext;

import static org.mockito.Mockito.mock;

@Factory
public class TestBeanFactory {

    @Bean
    @Singleton
    @Primary
    SfnClient sfnClient() {
        return mock(SfnClient.class);
    }

    @Bean
    @Singleton
    @Primary
    ReferenceNumberGenerator referenceNumberGenerator() {
        return mock(ReferenceNumberGenerator.class);
    }

    @Bean
    @Singleton
    @Primary
    DynamoDbClient dynamoDbClient() {
        return mock(DynamoDbClient.class);
    }

    @Bean
    @Singleton
    @Primary
    SecurityContext securityContext() {
        return mock(SecurityContext.class);
    }
}
