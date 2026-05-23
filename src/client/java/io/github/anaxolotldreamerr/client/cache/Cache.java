package io.github.anaxolotldreamerr.client.cache;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorites;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.HashSet;
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
        favoritesSet = new HashSet<>();
        ConfigManager manager = new ConfigManager();
        try {
            if (!manager.exists(filePath)) manager.write(filePath, null);
            JsonNode nodes = manager.read(filePath);
            for(JsonNode node : nodes) {
                String name = node.get("name").asText();
                String id = node.get("id").asText();
                Set<Identifier> identifiers = new HashSet<Identifier>();
                for (JsonNode object : node.get("objects")) {
                    identifiers.add(new ObjectMapper().enable(JsonParser.Feature.IGNORE_UNDEFINED).readerFor(Identifier.class).readValue(object.asText()));
                }
                favoritesSet.add(new Favorites<T>(name,id,(Set<T>)identifiers));
            }
        }catch (IOException e){
            ChatUtil.sendException(e);
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
    public boolean addFavorites(Favorites<T> favorites){
        if(!favoritesSet.contains(favorites)) favoritesSet.add(favorites);
        else {
            ChatUtil.sendWarning("don't repeat the addition");
            return false;
        }
        try {
            new ConfigManager().write(filePath,favoritesSet);
            return true;
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
