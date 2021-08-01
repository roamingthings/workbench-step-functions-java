package de.roamingthings.jokes.fn.retrieve.jokeapi;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

@Client(JokeApiConfiguration.JOKE_API_URL)
@Header(name = USER_AGENT, value = "Micronaut HTTP Client")
@Header(name = ACCEPT, value = "application/json")
@Introspected
public interface JokeApiClient {

    @Get("/Programming?type=${joke-api.type}")
    Single<Joke> fetchJoke();
}
