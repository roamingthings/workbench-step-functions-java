package de.roamingthings.jokes.fn.status;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.HashMap;
import java.util.Map;

@Controller
@Introspected
public class JobStatusController {

    private static final Logger log = LoggerFactory.getLogger(JobStatusController.class);

    private static final String STATUS_COLUMN = "Status";
    private static final String TEXT_COLUMN = "Text";

    @Get("/jobs/{jobId}")
    public JobStatus retrieveJobStatus(@QueryValue("jobId") String jobId) {
        log.info("Getting status for job <{}>", jobId);

        return getItem(jobId);
    }

    private DynamoDbClient getClient() {
        return DynamoDbClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    // Get a single item from the Work table based on the Key.
    public JobStatus getItem(String idValue) {
        var jobTable = System.getenv("JOB_TABLE");

        var ddbClient = getClient();

        var keyToGet = new HashMap<String, AttributeValue>();
        keyToGet.put("Id", AttributeValue.builder()
                .s(idValue)
                .build());

        var request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(jobTable)
                .build();

        GetItemResponse retrievedItem = ddbClient.getItem(request);
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

/*
  // All log statements are written to CloudWatch
  console.info("received:", event);
  console.info("queryStringParameters:", event.queryStringParameters);
  console.info("pathParameters:", event.pathParameters);

  const dynamoClient = new CustomDynamoClient();
  const jobId = event.pathParameters.proxy;
  if (jobId == null) {
    const response = {
      statusCode: 404,
    } as APIGatewayProxyResult;

    return response;

  }
  const jobStatus = await dynamoClient.read(jobId)
  if (jobStatus == null) {
    const response = {
      statusCode: 404,
    } as APIGatewayProxyResult;

    return response;

  }

  console.info(
    `jobStatus: ${jobStatus}`
  );

  const response = {
    statusCode: 200,
    body: JSON.stringify({
      id: jobStatus.Id,
      status: jobStatus.Status,
      text: jobStatus.Text,
    }),
  } as APIGatewayProxyResult;

  return response;
*/
