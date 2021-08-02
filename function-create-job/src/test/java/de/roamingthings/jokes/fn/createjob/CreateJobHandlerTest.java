package de.roamingthings.jokes.fn.createjob;

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;

@MicronautTest
class CreateJobHandlerTest {

    private static final String REFERENCE_NUMBER = "c6939f59-97d5-4359-a363-7d42463f1865";

    private static final Context lambdaContext = new MockLambdaContext();

    private static CreateJobHandler handler;
    private static ReferenceNumberGenerator referenceNumberGenerator;

    @BeforeAll
    public static void setupSpec() {
        handler = new CreateJobHandler();
        referenceNumberGenerator = handler.getApplicationContext().getBean(ReferenceNumberGenerator.class);
    }

    @AfterAll
    public static void cleanupSpec() {
        handler.getApplicationContext().close();
    }

    @Test
    void should_respond_with_reference_number() {
        doReturn(REFERENCE_NUMBER).when(referenceNumberGenerator).generateReferenceNumber();
        AwsProxyRequest request = new AwsProxyRequestBuilder("/jobs", HttpMethod.POST.toString())
                .method(HttpMethod.POST.toString())
                .path("/jobs")
                .header("Host", "testdomain.de")
                .stage("Test")
                .build();

        AwsProxyResponse response = handler.handleRequest(request, lambdaContext);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.getCode());
            softly.assertThat(response.getBody()).isEqualTo("{ \"ref\": \"" + REFERENCE_NUMBER + "\" }");
            softly.assertThat(response.getMultiValueHeaders().get("Location").get(0)).isEqualTo("https://testdomain.de/Test/jobs/c6939f59-97d5-4359-a363-7d42463f1865");
        });
    }

    @Test
    void should_respond_without_location_if_no_host_in_request() {
        doReturn(REFERENCE_NUMBER).when(referenceNumberGenerator).generateReferenceNumber();
        AwsProxyRequest request = new AwsProxyRequestBuilder("/jobs", HttpMethod.POST.toString())
                .method(HttpMethod.POST.toString())
                .path("/jobs")
                .stage("Test")
                .build();

        AwsProxyResponse response = handler.handleRequest(request, lambdaContext);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.getCode());
            softly.assertThat(response.getBody()).isEqualTo("{ \"ref\": \"" + REFERENCE_NUMBER + "\" }");
            softly.assertThat(response.getMultiValueHeaders()).isNull();
        });
    }
}
