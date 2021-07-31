package de.roamingthings.jokes.fn.retrieve;

import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public class Joke {

    private Integer formatVersion;
    private String category;
    private String type;
    private String joke;
    private JokeFlags flags;
    private String lang;

    public Integer getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(Integer formatVersion) {
        this.formatVersion = formatVersion;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public JokeFlags getFlags() {
        return flags;
    }

    public void setFlags(JokeFlags flags) {
        this.flags = flags;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "Joke{" +
                "formatVersion=" + formatVersion +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", joke='" + joke + '\'' +
                ", flags=" + flags +
                ", lang='" + lang + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joke joke1 = (Joke) o;
        return Objects.equals(formatVersion, joke1.formatVersion) && Objects.equals(category, joke1.category) && Objects.equals(type, joke1.type) && Objects.equals(joke, joke1.joke) && Objects.equals(flags, joke1.flags) && Objects.equals(lang, joke1.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formatVersion, category, type, joke, flags, lang);
    }
}
/*
{
    "formatVersion": 3,
    "category": "Misc",
    "type": "single",
    "joke": "",
    "flags": {
        "nsfw": false,
        "religious": false,
        "political": false,
        "racist": false,
        "sexist": false,
        "explicit": false
    },
    "lang": "en"
}
*/
