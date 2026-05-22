package io.github.anaxolotldreamerr.client.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorites;
import io.github.anaxolotldreamerr.client.util.ChatUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class Cache<T extends Identifier> {
    private Set<Favorites<T>> favoritesSet;
    private final String filePath;

    public Cache(String filePath) {
        this.filePath = filePath;
        load();
    }
    public static <U extends Identifier> Cache<U> getInstance(String filePath){
            return new Cache<>(filePath);
    }
    @SuppressWarnings("unchecked")
    public void load()  {
        ConfigManager manager = new ConfigManager();
        try {
            if (!manager.exists(filePath)) manager.write(filePath, null);
            favoritesSet = new ObjectMapper()
                    .readValue(manager
                            .read(filePath)
                            .asText(), new TypeReference<>() {
                    });
        }catch (IOException e){
            favoritesSet = Collections.EMPTY_SET;
        }
    }
    public void removeFavorites(Favorites<T> favorites){
        favoritesSet.remove(favorites);
        try {
            new ConfigManager().write(filePath,favoritesSet);
        } catch (IOException e) {
            throw new  IllegalStateException(e);
        }
    }
    public void addFavorites(Favorites<T> favorites){
        favoritesSet.add(favorites);
        try {
            new ConfigManager().write(filePath,favoritesSet);
        } catch (IOException e){
            throw new IllegalStateException(e);
        }
    }
    public Set<Favorites<T>> favoritesSet(){
        return favoritesSet;
    }
    public void save() throws IOException{
        new ConfigManager().write(filePath,favoritesSet);

    }
}
