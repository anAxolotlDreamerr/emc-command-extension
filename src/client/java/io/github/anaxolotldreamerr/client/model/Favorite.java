package io.github.anaxolotldreamerr.client.model;

import com.fasterxml.jackson.annotation.*;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class Favorite<T extends Identifier> {
    private final String name;
    private final String id;
    private final Set<T> objects;
    @JsonCreator
    public Favorite(
            @NotNull @JsonProperty("name") String name
            , @NotNull @JsonProperty("id") String id
            ,  @JsonProperty("objects") Set<T> objects) {
        if(id.isEmpty()) throw new IllegalArgumentException("Id can't be empty in the favorites:"+name);
        if(name.isEmpty()) throw new IllegalArgumentException("Name can't be empty in the favorites:"+id);
        this.name =name;
        this.id = id;
        this.objects = (objects == null) ? new HashSet<>() : new HashSet<>(objects);
    }
    @JsonGetter("id")
    public String id() {
        return id;
    }
    @JsonGetter("name")
    public String name() {
        return name;
    }
    @JsonGetter("objects")
    public Set<T> objects(){
        return objects;
    }
    public Favorite<T> add(T t,Cache<T> cache){
        objects.add(t);
        try {
            cache.save();
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't save the change");
        }
        return this;
    }
    public Set<T> addAll(Set<T> objects, Cache<T> cache){
        Set<T> added = new HashSet<>();
        for(T t : objects){
            if(!this.objects.contains(t)){
                this.objects.add(t);
                added.add(t);
            }
        }
        try {
            cache.save();
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't save the change");
        }
        return added;
    }
    public Favorite<T> remove(T t,Cache<T> cache){
        objects.remove(t);
        try {
            cache.save();
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't save the change");
        }
        return this;
    }
    public Set<T> removeAll(Set<T> t,Cache<T> cache){
        Set<T> removed = new HashSet<>();
        for(T o : t){
            if(objects.contains(o)){
                objects.remove(o);
                removed.add(o);
            }
        }
        try {
            cache.save();
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't save the change");
        }
        return removed;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Favorite favorite)) return false;
        return Objects.equals(name, favorite.name) || Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return 13;
    }

    @Override
    public String toString() {
        return "Favorites{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
