package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Introspected
public class RetrieveJokeHandler extends MicronautRequestHandler<RetrieveJokeJob, JokeRetrieved> {

    private static final Logger log = LoggerFactory.getLogger(RetrieveJokeHandler.class);

    @Inject
    private JokeApiClient jokeApiClient;

    @Override
    public JokeRetrieved execute(RetrieveJokeJob input) {
        Joke joke = jokeApiClient.fetchJoke().blockingGet();

        log.info("Retrieved joke {}", joke);

        JokeRetrieved response = new JokeRetrieved();
        response.setId(input.getRef());
        response.setTimestamp(getISO8601StringForDate(new Date()));
        response.setText(joke.getJoke());
        return response;
    }

    private static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }
/*
    var date = new Date();

    const res = await axios.get('https://v2.jokeapi.dev/joke/Programming?type=single');

    const joke = res.data.joke;
    console.log(joke);

    let result = {
        'id': event.ref,
        'text': joke,
        'timestamp': date.toISOString(),
    }

    console.info(`retrieve joke: ${JSON.stringify(result)}`);
    return result;
*/
}
