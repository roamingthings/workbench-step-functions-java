package de.roamingthings.jokes.fn.status;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
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

    @Get("/app/jobs/{jobId}")
    public HttpResponse<JobStatus> retrieveJobStatus(@QueryValue("jobId") String jobId) {
        log.info("Getting status for job <{}>", jobId);

        var jobStatus = jobStatusRepository.fetchJobStatusById(jobId);
        if (jobStatus == null) {
            return HttpResponse.notFound();
        } else {
            return HttpResponse.ok(jobStatus);
        }
    }
}
