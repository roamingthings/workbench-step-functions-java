package de.roamingthings.jokes.fn.createjob;

import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent.ProxyRequestContext;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.micronaut.http.HttpMethod.POST;
import static java.util.Collections.singletonList;
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
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Host", singletonList("testdomain.de"));
        Map<String, Object> authorizer = new HashMap<>();
        authorizer.put("principalId", "testPrincipal");
        var requestContext = new ProxyRequestContext()
                .withStage("Test");
        requestContext.setAuthorizer(authorizer);
        var request = new APIGatewayProxyRequestEvent()
                .withHttpMethod(POST.toString())
                .withPath("/jobs")
                .withRequestContext(requestContext)
                .withMultiValueHeaders(headers);

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, lambdaContext);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.getCode());
            softly.assertThat(response.getBody()).isEqualTo("{ \"ref\": \"" + REFERENCE_NUMBER + "\" }");
            softly.assertThat(response.getHeaders().get("Location")).isEqualTo("https://testdomain.de/Test/jobs/c6939f59-97d5-4359-a363-7d42463f1865");
        });
    }

    @Test
    void should_respond_without_location_if_no_host_in_request() {
        doReturn(REFERENCE_NUMBER).when(referenceNumberGenerator).generateReferenceNumber();
        Map<String, Object> authorizer = new HashMap<>();
        authorizer.put("principalId", "testPrincipal");
        var requestContext = new ProxyRequestContext()
                .withStage("Test");
        requestContext.setAuthorizer(authorizer);
        var request = new APIGatewayProxyRequestEvent()
                .withHttpMethod(POST.toString())
                .withPath("/jobs")
                .withRequestContext(requestContext);

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, lambdaContext);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.getCode());
            softly.assertThat(response.getBody()).isEqualTo("{ \"ref\": \"" + REFERENCE_NUMBER + "\" }");
            softly.assertThat(response.getMultiValueHeaders()).isNull();
        });
    }
}
