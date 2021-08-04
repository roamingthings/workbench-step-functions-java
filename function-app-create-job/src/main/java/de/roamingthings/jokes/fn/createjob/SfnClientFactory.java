package de.roamingthings.jokes.fn.createjob;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Introspected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.services.sfn.SfnClient;

import javax.inject.Singleton;

import static software.amazon.awssdk.regions.Region.EU_CENTRAL_1;

@Factory
@Introspected
public class SfnClientFactory {

    private static final Logger log = LoggerFactory.getLogger(SfnClientFactory.class);

    @Bean
    @Singleton
    @Requires(env = "aws")
    SfnClient sfnClientProd() {
        log.info("Setting AWS Credentials from Attached Role attached to Lambda Function.");
        log.info("See this - https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html");
        return SfnClient.builder()
                .region(EU_CENTRAL_1)
                .build();
    }

    @Bean
    @Singleton
    @Requires(notEnv = "aws")
    SfnClient sfnClient() {
        log.info("Setting EnvironmentVariableCredentialsProvider for AWS Credentials which would fetch credentials from Environment variables.");
        log.info("See this to set AWS Credentials Locally - https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html");
        return SfnClient.builder()
                .region(EU_CENTRAL_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}
