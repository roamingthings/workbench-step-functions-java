package de.roamingthings.jokes.fn.retrieve.jokeapi;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@ConfigurationProperties(JokeApiConfiguration.PREFIX)
@Requires(property = JokeApiConfiguration.PREFIX)
@Introspected
@Data
public class JokeApiConfiguration {

    public static final String PREFIX = "joke-api";
    public static final String JOKE_API_URL = "https://v2.jokeapi.dev/joke";

    private String type;
}
