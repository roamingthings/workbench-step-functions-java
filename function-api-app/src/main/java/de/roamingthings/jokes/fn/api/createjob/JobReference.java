package de.roamingthings.jokes.fn.api.createjob;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;

@Introspected
@Data
@AllArgsConstructor
public class JobReference {

    private String ref;
}
