package de.roamingthings.jokes.fn.createjob;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest;
import software.amazon.lambda.powertools.logging.LoggingUtils;

import javax.inject.Inject;

import static io.micronaut.http.HttpStatus.CREATED;

@Introspected
public class CreateJobHandler extends MicronautRequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final Logger log = LoggerFactory.getLogger(CreateJobHandler.class);

    @Value("${RETRIEVE_JOKE_STATE_MACHINE_ARN}")
    private String stateMachineArn;

    @Inject
    private SfnClient sfnClient;

    @Inject
    private ReferenceNumberGenerator referenceNumberGenerator;

    @Override
    public AwsProxyResponse execute(AwsProxyRequest input) {
        var referenceNumber = referenceNumberGenerator.generateReferenceNumber();

        LoggingUtils.appendKey("jokeJobReferenceNumber", referenceNumber);
        log.info("Starting joke job");

        startWorkflow(stateMachineArn, referenceNumber);

        return createResponse(input, referenceNumber);
    }

    public void startWorkflow(String stateMachineArn, String referenceNumber) {
        var executionRequest = StartExecutionRequest.builder()
                .input(createJobReferenceJson(referenceNumber))
                .stateMachineArn(stateMachineArn)
                .name(referenceNumber)
                .build();

        sfnClient.startExecution(executionRequest);
    }

    private AwsProxyResponse createResponse(AwsProxyRequest input, String referenceNumber) {
        var response = new AwsProxyResponse(CREATED.getCode());
        response.setBody(createJobReferenceJson(referenceNumber));
        var locationBaseUri = locationBaseUriOf(input);
        if (locationBaseUri != null) {
            response.addHeader("Location", locationBaseUri + "/" + referenceNumber);
        }
        return response;
    }

    private String locationBaseUriOf(AwsProxyRequest input) {
        String domainName = domainNameFrom(input);
        if (domainName != null) {
            return "https://" + domainName + "/" + input.getRequestContext().getStage() + input.getPath();
        } else {
            return null;
        }
    }

    private String domainNameFrom(AwsProxyRequest input) {
        var hostHeaders = input.getMultiValueHeaders().get("Host");
        return (hostHeaders != null && !hostHeaders.isEmpty()) ? hostHeaders.get(0) : null;
    }

    private static String createJobReferenceJson(String referenceNumber) {
        return "{ \"ref\": \"" + referenceNumber + "\" }";
    }
}
