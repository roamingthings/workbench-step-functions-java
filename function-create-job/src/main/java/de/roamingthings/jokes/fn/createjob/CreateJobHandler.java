package de.roamingthings.jokes.fn.createjob;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest;
import software.amazon.lambda.powertools.logging.LoggingUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static io.micronaut.http.HttpStatus.CREATED;

@Introspected
public class CreateJobHandler extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger log = LoggerFactory.getLogger(CreateJobHandler.class);

    @Value("${RETRIEVE_JOKE_STATE_MACHINE_ARN}")
    private String stateMachineArn;

    @Inject
    private SfnClient sfnClient;

    @Inject
    private ReferenceNumberGenerator referenceNumberGenerator;

    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent input) {
        var principalId = input.getRequestContext().getAuthorizer().get("principalId").toString();
        var tokenName = input.getRequestContext().getAuthorizer().get("tokenName");
        log.info("Authorization Context: principalId={}, name from token={}", principalId, tokenName);

        var referenceNumber = referenceNumberGenerator.generateReferenceNumber();

        LoggingUtils.appendKey("jokeJobReferenceNumber", referenceNumber);
        log.info("Starting joke job");

        startWorkflow(stateMachineArn, referenceNumber, principalId);

        return createResponse(input, referenceNumber);
    }

    public void startWorkflow(String stateMachineArn, String referenceNumber, String principalId) {
        var executionRequest = StartExecutionRequest.builder()
                .input(createStateMachineInput(referenceNumber, principalId))
                .stateMachineArn(stateMachineArn)
                .name(referenceNumber)
                .build();

        sfnClient.startExecution(executionRequest);
    }

    private APIGatewayProxyResponseEvent createResponse(APIGatewayProxyRequestEvent input, String referenceNumber) {
        var response = new APIGatewayProxyResponseEvent()
                .withStatusCode(CREATED.getCode())
                .withBody(createJobReferenceJson(referenceNumber));
        var locationBaseUri = locationBaseUriOf(input);
        if (locationBaseUri != null) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Location", locationBaseUri + "/" + referenceNumber);
            response.setHeaders(headers);
        }
        return response;
    }

    private String locationBaseUriOf(APIGatewayProxyRequestEvent input) {
        String domainName = domainNameFrom(input);
        if (domainName != null) {
            return "https://" + domainName + "/" + input.getRequestContext().getStage() + input.getPath();
        } else {
            return null;
        }
    }

    private String domainNameFrom(APIGatewayProxyRequestEvent input) {
        var hostHeaders = (input.getMultiValueHeaders() != null) ? input.getMultiValueHeaders().get("Host") : null;
        return (hostHeaders != null && !hostHeaders.isEmpty()) ? hostHeaders.get(0) : null;
    }

    private static String createJobReferenceJson(String referenceNumber) {
        return "{ \"ref\": \"" + referenceNumber + "\" }";
    }

    private static String createStateMachineInput(String referenceNumber, String principalId) {
        return "{ \"ref\": \"" + referenceNumber + "\", \"akteur\": \"" + principalId + "\" }";
    }
}
