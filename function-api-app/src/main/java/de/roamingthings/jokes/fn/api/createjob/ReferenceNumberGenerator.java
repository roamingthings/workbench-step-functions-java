package de.roamingthings.jokes.fn.api.createjob;

import io.micronaut.core.annotation.Introspected;

import javax.inject.Singleton;

import static java.util.UUID.randomUUID;

@Singleton
@Introspected
public class ReferenceNumberGenerator {

    public String generateReferenceNumber() {
        return randomUUID().toString();
    }
}
