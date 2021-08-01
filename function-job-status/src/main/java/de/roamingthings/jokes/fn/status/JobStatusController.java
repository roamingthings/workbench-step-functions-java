package de.roamingthings.jokes.fn.status;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Controller
@Introspected
public class JobStatusController {

    private static final Logger log = LoggerFactory.getLogger(JobStatusController.class);

    @Inject
    private JobStatusRepository jobStatusRepository;

    @Get("/jobs/{jobId}")
    public JobStatus retrieveJobStatus(@QueryValue("jobId") String jobId) {
        log.info("Getting status for job <{}>", jobId);

        return jobStatusRepository.fetchJobStatusById(jobId);
    }
}

/*
  // All log statements are written to CloudWatch
  console.info("received:", event);
  console.info("queryStringParameters:", event.queryStringParameters);
  console.info("pathParameters:", event.pathParameters);

  const dynamoClient = new CustomDynamoClient();
  const jobId = event.pathParameters.proxy;
  if (jobId == null) {
    const response = {
      statusCode: 404,
    } as APIGatewayProxyResult;

    return response;

  }
  const jobStatus = await dynamoClient.read(jobId)
  if (jobStatus == null) {
    const response = {
      statusCode: 404,
    } as APIGatewayProxyResult;

    return response;

  }

  console.info(
    `jobStatus: ${jobStatus}`
  );

  const response = {
    statusCode: 200,
    body: JSON.stringify({
      id: jobStatus.Id,
      status: jobStatus.Status,
      text: jobStatus.Text,
    }),
  } as APIGatewayProxyResult;

  return response;
*/
