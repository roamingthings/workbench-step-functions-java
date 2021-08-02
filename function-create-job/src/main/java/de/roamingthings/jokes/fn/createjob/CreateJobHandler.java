package de.roamingthings.jokes.fn.createjob;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import io.micronaut.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest;

import javax.inject.Inject;

@Introspected
public class CreateJobHandler extends MicronautRequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final Logger log = LoggerFactory.getLogger(CreateJobHandler.class);

    @Inject
    private SfnClient sfnClient;

    @Inject
    private ReferenceNumberGenerator referenceNumberGenerator;

    @Override
    public AwsProxyResponse execute(AwsProxyRequest input) {
        var stateMachineArn = System.getenv("RETRIEVE_JOKE_STATE_MACHINE_ARN");
        var referenceNumber = referenceNumberGenerator.generateReferenceNumber();

        log.info("Starting job {}", referenceNumber);

        startWorkflow(stateMachineArn, referenceNumber);

        var response = new AwsProxyResponse(HttpStatus.CREATED.getCode());
        response.setBody(createJobReferenceJson(referenceNumber));
//        String location = "https://" + input.getRequestContext().getDomainName() + input.getRequestContext().getPath() + "/" + referenceNumber;
        String location = input.getRequestContext().getPath() + "/" + referenceNumber;
        response.addHeader("Location", location);
        return response;
    }

    private static String createJobReferenceJson(String referenceNumber) {
        return "{ \"ref\": \"" + referenceNumber + "\" }";
    }

    public void startWorkflow(String stateMachineArn, String referenceNumber) {
        var executionRequest = StartExecutionRequest.builder()
                .input(createJobReferenceJson(referenceNumber))
                .stateMachineArn(stateMachineArn)
                .name(referenceNumber)
                .build();

        sfnClient.startExecution(executionRequest);
    }
}
