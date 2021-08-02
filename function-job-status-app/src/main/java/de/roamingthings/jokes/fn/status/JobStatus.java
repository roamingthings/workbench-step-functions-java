package de.roamingthings.jokes.fn.status;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Introspected
@Data
public class JobStatus {

    private String id;
    private String status;
    private String text;
}
