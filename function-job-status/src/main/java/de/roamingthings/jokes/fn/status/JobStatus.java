package de.roamingthings.jokes.fn.status;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobStatus {

    private String id;
    private String status;
    private String text;
}
