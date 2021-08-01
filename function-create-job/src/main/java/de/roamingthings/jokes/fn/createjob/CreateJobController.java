package de.roamingthings.jokes.fn.createjob;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.proxy.MicronautAwsProxyRequest;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

import static java.util.UUID.randomUUID;

@Controller
@Introspected
public class CreateJobController {

    private static final Logger log = LoggerFactory.getLogger(CreateJobController.class);

    @Inject
    SfnClient sfnClient;

    @Post("/jobs")
    public HttpResponse<String> createJob(HttpRequest<Void> request) throws URISyntaxException {
        var stateMachineArn = System.getenv("RETRIEVE_JOKE_STATE_MACHINE_ARN");
        var referenceNumber = randomUUID().toString();

        log.info("Starting job {}", referenceNumber);

        startWorkflow(stateMachineArn, referenceNumber);

        if (request instanceof MicronautAwsProxyRequest) {
            var awsRequest = (MicronautAwsProxyRequest<Void>) request;
            String location = "https://" + request.getServerName() + awsRequest.getAwsProxyRequest().getRequestContext().getPath() + "/" + referenceNumber;
            return HttpResponse.created(createJobReferenceJson(referenceNumber), new URI(location));
        } else {
            return HttpResponse.created(createJobReferenceJson(referenceNumber));
        }
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
