package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    int id;
    String title;
    String link;
    String description;
    LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id
                && Objects.equals(title, post.title)
                && Objects.equals(description, post.description)
                && Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, title, description, created);
    }

    @Override
    public String toString() {
        return "Post{"
                + "link="
                + link
                + ", title='"
                + title
                + '\''
                + ", description='"
                + description
                + '\''
                + ", created="
                + created
                + '}';
    }
}