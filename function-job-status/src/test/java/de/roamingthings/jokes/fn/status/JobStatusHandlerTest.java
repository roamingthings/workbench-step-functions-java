package de.roamingthings.jokes.fn.status;

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpMethod.GET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.doReturn;

class JobStatusHandlerTest {

    private static final String JOB_ID = "abc-123";

    private static final Context lambdaContext = new MockLambdaContext();

    private static JobStatusHandler handler;
    private static JobStatusRepository jobStatusRepository;

    @BeforeAll
    public static void setupSpec() {
        System.setProperty("JOB_TABLE", "testTable");
        handler = new JobStatusHandler();
        jobStatusRepository = handler.getApplicationContext().getBean(JobStatusRepository.class);
    }

    @AfterAll
    public static void cleanupSpec() {
        handler.getApplicationContext().close();
    }

    @Test
    void should_respond_with_job_status_if_job_exists() {
        var jobStatus = new JobStatus(
                JOB_ID,
                "FINISHED",
                "This is not a joke"
        );
        doReturn(jobStatus).when(jobStatusRepository).fetchJobStatusById(JOB_ID);
        AwsProxyRequest request = new AwsProxyRequestBuilder("/jobs", GET.toString())
                .method(GET.toString())
                .build();
        request.getPathParameters().put("jobId", JOB_ID);

        AwsProxyResponse response = handler.handleRequest(request, lambdaContext);

        assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.getCode());
            softly.assertThat(response.getBody()).isEqualTo("{\"id\":\"" + JOB_ID + "\",\"status\":\"FINISHED\",\"text\":\"This is not a joke\"}");
        });
    }

    @Test
    void should_respond_with_NOT_FOUND_if_job_doesnt_exists() {
        AwsProxyRequest request = new AwsProxyRequestBuilder("/jobs", GET.toString())
                .method(GET.toString())
                .build();
        request.getPathParameters().put("jobId", JOB_ID);

        AwsProxyResponse response = handler.handleRequest(request, lambdaContext);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }
}
