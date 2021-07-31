package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public class RetrieveJokeJob {
    private String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "RetrieveJokeJob{" +
                "ref='" + ref + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RetrieveJokeJob that = (RetrieveJokeJob) o;
        return Objects.equals(ref, that.ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ref);
    }
}
