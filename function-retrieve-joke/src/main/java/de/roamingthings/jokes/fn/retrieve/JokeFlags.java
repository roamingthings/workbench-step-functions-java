package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public class JokeFlags {

    private Boolean nsfw;
    private Boolean religious;
    private Boolean political;
    private Boolean racist;
    private Boolean sexist;
    private Boolean explicit;

    public Boolean getNsfw() {
        return nsfw;
    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    public Boolean getReligious() {
        return religious;
    }

    public void setReligious(Boolean religious) {
        this.religious = religious;
    }

    public Boolean getPolitical() {
        return political;
    }

    public void setPolitical(Boolean political) {
        this.political = political;
    }

    public Boolean getRacist() {
        return racist;
    }

    public void setRacist(Boolean racist) {
        this.racist = racist;
    }

    public Boolean getSexist() {
        return sexist;
    }

    public void setSexist(Boolean sexist) {
        this.sexist = sexist;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    @Override
    public String toString() {
        return "JokeFlags{" +
                "nsfw=" + nsfw +
                ", religious=" + religious +
                ", political=" + political +
                ", racist=" + racist +
                ", sexist=" + sexist +
                ", explicit=" + explicit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JokeFlags jokeFlags = (JokeFlags) o;
        return Objects.equals(nsfw, jokeFlags.nsfw) && Objects.equals(religious, jokeFlags.religious) && Objects.equals(political, jokeFlags.political) && Objects.equals(racist, jokeFlags.racist) && Objects.equals(sexist, jokeFlags.sexist) && Objects.equals(explicit, jokeFlags.explicit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nsfw, religious, political, racist, sexist, explicit);
    }
}
