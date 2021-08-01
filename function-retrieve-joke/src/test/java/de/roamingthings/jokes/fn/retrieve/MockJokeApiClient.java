package de.roamingthings.jokes.fn.retrieve;

import de.roamingthings.jokes.fn.retrieve.jokeapi.Joke;
import de.roamingthings.jokes.fn.retrieve.jokeapi.JokeApiClient;
import io.micronaut.context.annotation.Replaces;
import io.reactivex.Single;

import javax.inject.Singleton;

@Singleton
@Replaces(JokeApiClient.class)
public class MockJokeApiClient implements JokeApiClient {

    public static final String JOKE_TEXT = "This is not funny";

    @Override
    public Single<Joke> fetchJoke() {
        Joke joke = new Joke();
        joke.setJoke(JOKE_TEXT);
        return Single.just(joke);
    }
}
