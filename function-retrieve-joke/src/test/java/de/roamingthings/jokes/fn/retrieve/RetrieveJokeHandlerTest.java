package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static de.roamingthings.jokes.fn.retrieve.MockJokeApiClient.JOKE_TEXT;
import static de.roamingthings.jokes.fn.retrieve.TestBeanFactory.SYSTEM_TIME;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@MicronautTest
class RetrieveJokeHandlerTest {

    private static final String REFERENCE = "d74de98e-3a72-4978-9eb5-195800cb1ab8";

    private static RetrieveJokeHandler retrieveJokeHandler;

    @BeforeAll
    static void setupServer() {
        retrieveJokeHandler = new RetrieveJokeHandler();
    }

    @AfterAll
    static void stopServer() {
        if (retrieveJokeHandler != null) {
            retrieveJokeHandler.getApplicationContext().close();
        }
    }

    @Test
    void should_retrieve_joke() {
        var retrieveJokeEvent = new RetrieveJokeJob();
        retrieveJokeEvent.setRef(REFERENCE);

        var jokeRetrieved = retrieveJokeHandler.execute(retrieveJokeEvent);

        assertSoftly(softly -> {
            softly.assertThat(jokeRetrieved.getId()).isEqualTo(REFERENCE);
            softly.assertThat(jokeRetrieved.getText()).isEqualTo(JOKE_TEXT);
            softly.assertThat(jokeRetrieved.getTimestamp()).isEqualTo(SYSTEM_TIME);
        });
    }
}
