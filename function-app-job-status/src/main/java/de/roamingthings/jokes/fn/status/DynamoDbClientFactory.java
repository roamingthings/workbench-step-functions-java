package de.roamingthings.jokes.fn.status;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Introspected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

import javax.inject.Singleton;
import java.util.HashMap;

import static software.amazon.awssdk.regions.Region.EU_CENTRAL_1;

@Factory
@Introspected
public class DynamoDbClientFactory {

    private static final Logger log = LoggerFactory.getLogger(DynamoDbClientFactory.class);

    @Value("${JOB_TABLE}")
    private String jobTable;

    @Bean
    @Singleton
    @Requires(env = "aws")
    DynamoDbClient dynamoDbClientProd() {
        log.info("Setting AWS Credentials from Attached Role attached to Lambda Function.");
        log.info("See this - https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html");
        var dynamoDbClient = DynamoDbClient.builder()
                .region(EU_CENTRAL_1)
                .build();

        prePopulateClient(dynamoDbClient);

        return dynamoDbClient;
    }

    private void prePopulateClient(DynamoDbClient dynamoDbClient) {
        var keyToGet = new HashMap<String, AttributeValue>();
        keyToGet.put("Id", AttributeValue.builder()
                .s("warump")
                .build());

        var request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(jobTable)
                .build();

        try {
            dynamoDbClient.getItem(request);
        } catch (Exception ignored) {
            // Swallow exception. The purpose of getItem() is to prepopulate the Jackson ObjectMapper during startup
        }
    }

    @Bean
    @Singleton
    @Requires(notEnv = "aws")
    DynamoDbClient dynamoDbClient() {
        log.info("Setting EnvironmentVariableCredentialsProvider for AWS Credentials which would fetch credentials from Environment variables.");
        log.info("See this to set AWS Credentials Locally - https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html");
        return DynamoDbClient.builder()
                .region(EU_CENTRAL_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}
