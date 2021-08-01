package de.roamingthings.jokes.fn.createjob;

import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public class JobReference {
    private String ref;

    public JobReference() {
    }

    public JobReference(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "JobReference{" +
                "ref='" + ref + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobReference that = (JobReference) o;
        return Objects.equals(ref, that.ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ref);
    }
}
