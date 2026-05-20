package io.github.anaxolotldreamerr.client.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorites;
import io.github.anaxolotldreamerr.client.util.ChatUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class Cache<T extends Identifier> {
    private Set<Favorites<T>> favoritesSet;
    private final String filePath;
    public Cache(String filePath) throws IOException {
        this.filePath = filePath;
        load();
    }
    public static <U extends Identifier> Optional<Cache<U>> getInstance(String filePath){
        try {
            return Optional.of(new Cache<>(filePath));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
    public void load() throws IOException {
        ConfigManager manager = new ConfigManager();
        if(!manager.exists(filePath))manager.write(filePath,null);
        favoritesSet =new ObjectMapper()
                .readValue(manager
                        .read(filePath)
                        .asText(), new TypeReference<>() {});
    }
    public void removeFavorites(Favorites<T> favorites){
        favoritesSet.remove(favorites);
        try {
            new ConfigManager().write(filePath,favoritesSet);
        } catch (IOException e) {
            ChatUtil.sendException(e);
        }
    }
    public void addFavorites(Favorites<T> favorites){
        favoritesSet.add(favorites);
        try {
            new ConfigManager().write(filePath,favoritesSet);
        } catch (IOException e){
            ChatUtil.sendException(e);
        }
    }
    public Set<Favorites<T>> favoritesSet(){
        return favoritesSet;
    }
    public void update(){
        try {
            new ConfigManager().write(filePath,favoritesSet);
        } catch (IOException e){
            ChatUtil.sendException(e);
        }
    }
}
