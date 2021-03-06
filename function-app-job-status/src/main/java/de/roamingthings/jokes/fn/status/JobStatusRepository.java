package de.roamingthings.jokes.fn.status;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Introspected;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Introspected
public class JobStatusRepository {

    private static final String STATUS_COLUMN = "Status";
    private static final String TEXT_COLUMN = "Text";

    @Value("${JOB_TABLE}")
    private String jobTable;

    private final DynamoDbClient dynamoDbClient;

    public JobStatusRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public JobStatus fetchJobStatusById(String idValue) {
        var keyToGet = new HashMap<String, AttributeValue>();
        keyToGet.put("Id", AttributeValue.builder()
                .s(idValue)
                .build());

        var request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(jobTable)
                .build();

        GetItemResponse retrievedItem = dynamoDbClient.getItem(request);
        if (!retrievedItem.hasItem()) {
            return null;
        }

        Map<String, AttributeValue> returnedItem = retrievedItem.item();
        var jobStatus = new JobStatus();
        jobStatus.setId(idValue);
        if (returnedItem.containsKey(STATUS_COLUMN)) {
            jobStatus.setStatus(returnedItem.get(STATUS_COLUMN).s());
        }
        if (returnedItem.containsKey(TEXT_COLUMN)) {
            jobStatus.setText(returnedItem.get(TEXT_COLUMN).s());
        }
        return jobStatus;
    }
}
