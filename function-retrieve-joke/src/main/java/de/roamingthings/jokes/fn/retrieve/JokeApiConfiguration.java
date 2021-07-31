package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@ConfigurationProperties(JokeApiConfiguration.PREFIX)
@Requires(property = JokeApiConfiguration.PREFIX)
@Introspected
public class JokeApiConfiguration {
    public static final String PREFIX = "joke-api";
    public static final String JOKE_API_URL = "https://v2.jokeapi.dev/joke";

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "JokeApiConfiguration{" +
                "type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JokeApiConfiguration that = (JokeApiConfiguration) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
