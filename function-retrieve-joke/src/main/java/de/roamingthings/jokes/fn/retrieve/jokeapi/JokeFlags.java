package de.roamingthings.jokes.fn.retrieve.jokeapi;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.util.Objects;

@Introspected
@Data
public class JokeFlags {

    private Boolean nsfw;
    private Boolean religious;
    private Boolean political;
    private Boolean racist;
    private Boolean sexist;
    private Boolean explicit;
}
