package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JokeRetrieved {

    private String id;
    private String text;
    // The serialization to JSON seams to happen outside the Micronaut application
    // and the timestamp format cannot be configured. So a String is used instead of Instant
    private String timestamp;
}
