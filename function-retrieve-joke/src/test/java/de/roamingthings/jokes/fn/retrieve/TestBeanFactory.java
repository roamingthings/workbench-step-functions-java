package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;

import javax.inject.Singleton;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Factory
public class TestBeanFactory {

    public static final String SYSTEM_TIME = "2021-08-01T10:15:30Z";

    @Singleton
    @Bean
    @Primary
    Clock systemClock() {
        return Clock.fixed(Instant.parse(SYSTEM_TIME), ZoneId.systemDefault());
    }
}
