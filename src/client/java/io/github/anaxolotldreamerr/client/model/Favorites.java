package io.github.anaxolotldreamerr.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Favorites <T extends Identifier> {
    private final String name;
    private final String id;
    private Set<T> objects;
    @JsonCreator
    public Favorites(
            @NotNull @JsonProperty("name") String name
            , @NotNull @JsonProperty("id") String id
            , @NotNull @JsonProperty("objects") Set<T> objects) {
        if(id.isEmpty()) throw new IllegalArgumentException("Id can't be empty in the favorites:"+name);
        if(name.isEmpty()) throw new IllegalArgumentException("Name can't be empty in the favorites:"+id);
        if(objects.isEmpty()) throw new IllegalArgumentException("Objects can't be empty in the favorites:"+id+"-"+name);
        this.name =name;
        this.id = id;
        this.objects = Set.copyOf(objects);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Favorites<T> add(T t){
        objects.add(t);
        return this;
    }
    public Favorites<T> remove(T t){
        objects.remove(t);
        return this;
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
