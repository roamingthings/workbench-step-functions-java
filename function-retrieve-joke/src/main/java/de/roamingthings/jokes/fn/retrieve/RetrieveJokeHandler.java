package de.roamingthings.jokes.fn.retrieve;

import de.roamingthings.jokes.fn.retrieve.jokeapi.Joke;
import de.roamingthings.jokes.fn.retrieve.jokeapi.JokeApiClient;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Clock;

import static java.time.Instant.now;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

@Introspected
public class RetrieveJokeHandler extends MicronautRequestHandler<RetrieveJokeJob, JokeRetrieved> {

    private static final Logger log = LoggerFactory.getLogger(RetrieveJokeHandler.class);

    @Inject
    private JokeApiClient jokeApiClient;

    @Inject
    private Clock systemClock;

    @Override
    public JokeRetrieved execute(RetrieveJokeJob input) {
        Joke joke = jokeApiClient.fetchJoke().blockingGet();

        log.info("Retrieved joke {}", joke);

        return new JokeRetrieved(
                input.getRef(),
                joke.getJoke(),
                ISO_INSTANT.format(now(systemClock)));
    }
}
