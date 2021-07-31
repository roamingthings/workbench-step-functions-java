package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public class JokeRetrieved {
    private String id;
    private String text;
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "JokeRetrieved{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JokeRetrieved that = (JokeRetrieved) o;
        return Objects.equals(id, that.id) && Objects.equals(text, that.text) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, timestamp);
    }
}
