package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Introspected
@Data
public class RetrieveJokeJob {

    private String ref;
}
