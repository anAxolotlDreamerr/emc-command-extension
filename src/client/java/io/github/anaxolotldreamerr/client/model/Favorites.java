package io.github.anaxolotldreamerr.client.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Favorites {
    private String name;
    private String id;
    public Favorites(@NotNull String name,@NotNull String id){
        if(id.isEmpty()) throw new IllegalArgumentException("Id can't be empty in the favorites:"+name);
        if(name.isEmpty()) throw new IllegalArgumentException("Name can't be empty in the favorites:"+id);
        this.name =name;
        this.id = id;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Favorites favorites)) return false;
        return Objects.equals(name, favorites.name) || Objects.equals(id, favorites.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return "Favorites{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
