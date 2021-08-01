package de.roamingthings.jokes.fn.retrieve.jokeapi;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Introspected
@Data
public class Joke {

    private Integer formatVersion;
    private String category;
    private String type;
    private String joke;
    private JokeFlags flags;
    private String lang;
}
