package de.roamingthings.jokes.fn.authorizer;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.Introspected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import javax.inject.Singleton;


@Factory
@Introspected
public class SecretsManagerClientFactory {

    private static final Logger log = LoggerFactory.getLogger(SecretsManagerClientFactory.class);

    @Bean
    @Singleton
    public SecretsManagerClient secretsManagerClient() {
        log.info("Setting AWS Credentials from Attached Role attached to Lambda Function.");
        log.info("See this - https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html");
        return SecretsManagerClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
    }
}
