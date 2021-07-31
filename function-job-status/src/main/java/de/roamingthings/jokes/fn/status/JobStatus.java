package de.roamingthings.jokes.fn.status;

import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public class JobStatus {
    private String id;
    private String status;
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "JobStatus{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobStatus jobStatus = (JobStatus) o;
        return Objects.equals(id, jobStatus.id) && Objects.equals(status, jobStatus.status) && Objects.equals(text, jobStatus.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, text);
    }
}
