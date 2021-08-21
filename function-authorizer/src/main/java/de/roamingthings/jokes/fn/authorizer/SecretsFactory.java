package de.roamingthings.jokes.fn.authorizer;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Introspected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

@Factory
@Introspected
public class SecretsFactory {

    public static final Logger log = LoggerFactory.getLogger(SecretsFactory.class);

    private final String secretId;

    private final SecretsManagerClient secretsManagerClient;

    @Inject
    public SecretsFactory(@Value("${SECRET_ID}") String secretId, SecretsManagerClient secretsManagerClient) {
        this.secretId = secretId;
        this.secretsManagerClient = secretsManagerClient;

        log.info("Secret id: {}", secretId);
        log.info("Client: {}", secretsManagerClient);
    }

    @Bean
    @Singleton
    public String jwtPublicKey() {
        var secretValue = secretsManagerClient.getSecretValue(
                GetSecretValueRequest.builder().secretId(secretId).build()
        );

        var jwtPublicKey = secretValue.secretString();
        log.info("Secret value: {}", jwtPublicKey);

        return jwtPublicKey;
    }
}
