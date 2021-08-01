package de.roamingthings.jokes.fn.createjob;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.function.aws.proxy.MicronautLambdaHandler;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@MicronautTest
class CreateJobControllerTest {

    private static final String REFERENCE_NUMBER = "c6939f59-97d5-4359-a363-7d42463f1865";

    private static final Context lambdaContext = new MockLambdaContext();
    private static MicronautLambdaHandler handler;
    private static ObjectMapper objectMapper;

    private static ReferenceNumberGenerator referenceNumberGenerator;

    @BeforeAll
    public static void setupSpec() {
        try {
            handler = new MicronautLambdaHandler();
            objectMapper = handler.getApplicationContext().getBean(ObjectMapper.class);
            referenceNumberGenerator = handler.getApplicationContext().getBean(ReferenceNumberGenerator.class);
        } catch (ContainerInitializationException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void cleanupSpec() {
        handler.getApplicationContext().close();
    }

    @Test
    void should_respond_with_reference_number() throws JsonProcessingException {
        doReturn(REFERENCE_NUMBER).when(referenceNumberGenerator).generateReferenceNumber();
        AwsProxyRequest request = new AwsProxyRequestBuilder("/jobs", HttpMethod.POST.toString())
                .build();

        AwsProxyResponse response = handler.handleRequest(request, lambdaContext);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.getCode());
        JobReference jobReference = objectMapper.readValue(response.getBody(), JobReference.class);
        assertThat(jobReference.getRef()).isEqualTo(REFERENCE_NUMBER);
    }
}
