package de.roamingthings.jokes.fn.createjob;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;

@Introspected
@Data
@AllArgsConstructor
public class JobReference {

    private String ref;
}
