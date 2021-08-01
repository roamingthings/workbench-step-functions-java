package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.Introspected;

import javax.inject.Singleton;
import java.time.Clock;

@Factory
@Introspected
public class SystemClockFactory {

    @Singleton
    @Bean
    Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}
