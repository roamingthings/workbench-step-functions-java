package de.roamingthings.jokes.fn.createjob;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.Introspected;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.services.sfn.SfnClient;

@Factory
@Introspected
public class SfnClientFactory {

    @Bean
    SfnClient sfnClient() {
        return SfnClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}
