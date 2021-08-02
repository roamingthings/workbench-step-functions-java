package de.roamingthings.jokes.fn.status;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.Headers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Introspected
public class JobStatusHandler extends MicronautRequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final Logger log = LoggerFactory.getLogger(JobStatusHandler.class);

    @Inject
    private JobStatusRepository jobStatusRepository;

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public AwsProxyResponse execute(AwsProxyRequest input) {
        var jobId = input.getPathParameters().get("jobId");
        if (jobId == null || jobId.isBlank()) {
            return new AwsProxyResponse(HttpStatus.NOT_FOUND.getCode());
        }

        log.info("Getting status for job <{}>", jobId);

        var jobStatus = jobStatusRepository.fetchJobStatusById(jobId);

        if (jobStatus == null) {
            return new AwsProxyResponse(HttpStatus.NOT_FOUND.getCode());
        } else {
            Headers headers = new Headers();
            headers.add("Content-Type", MediaType.APPLICATION_JSON);
            try {
                return new AwsProxyResponse(HttpStatus.OK.getCode(), headers, objectMapper.writeValueAsString(jobStatus));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize response: ", e);
                return new AwsProxyResponse(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
            }
        }
    }
}
