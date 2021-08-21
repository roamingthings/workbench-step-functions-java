package de.roamingthings.jokes.fn.createjob;

import com.amazonaws.serverless.proxy.internal.jaxrs.AwsProxySecurityContext;
import io.micronaut.context.annotation.Value;
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
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@Controller
@Introspected
public class CreateJobController {

    private static final Logger log = LoggerFactory.getLogger(CreateJobController.class);

    @Value("${RETRIEVE_JOKE_STATE_MACHINE_ARN}")
    private String stateMachineArn;

    @Inject
    private SfnClient sfnClient;

    @Inject
    private ReferenceNumberGenerator referenceNumberGenerator;

    @Post("/jobs")
    public HttpResponse<JobReference> createJob(HttpRequest<Void> request) throws URISyntaxException {
        var referenceNumber = referenceNumberGenerator.generateReferenceNumber();

        log.info("Starting job {}", referenceNumber);

        if (request instanceof MicronautAwsProxyRequest) {
            var awsRequest = (MicronautAwsProxyRequest<Void>) request;
            var principalId = awsRequest.getAwsProxyRequest().getRequestContext().getAuthorizer().getPrincipalId();

            startWorkflow(stateMachineArn, referenceNumber, principalId);

            String location = "https://" + request.getServerName() + awsRequest.getAwsProxyRequest().getRequestContext().getPath() + "/" + referenceNumber;
            return HttpResponse.created(new JobReference(referenceNumber), new URI(location));
        } else {
            return HttpResponse.created(new JobReference(referenceNumber));
        }
    }

    public void startWorkflow(String stateMachineArn, String referenceNumber, String principalId) {
        var executionRequest = StartExecutionRequest.builder()
                .input(createStateMachineInput(referenceNumber, principalId))
                .stateMachineArn(stateMachineArn)
                .name(referenceNumber)
                .build();

        sfnClient.startExecution(executionRequest);
    }

    private static String createStateMachineInput(String referenceNumber, String principalId) {
        return "{ \"ref\": \"" + referenceNumber + "\", \"akteur\": \"" + principalId + "\" }";
    }
}
