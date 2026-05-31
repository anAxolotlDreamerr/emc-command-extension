package io.github.anaxolotldreamerr.client.cache;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;
import io.github.anaxolotldreamerr.client.util.ChatUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Cache<T extends Identifier> {
    private Set<Favorite<T>> favoriteSet;
    private final String filePath;
    private static volatile Set<TownIdentifier> townIdentifiers;
    private static ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    private static boolean isStarting = false;
    public static void start(){
            isStarting=true;
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    townIdentifiers = EMCApiRequest.getTownIdentifiers();
                    ChatUtil.sendWarning(townIdentifiers.toString());
                }catch (Exception e){
                    ChatUtil.sendException(e);
                }

            }, 0, 10, TimeUnit.SECONDS);
    }
    public static synchronized void update(Set<TownIdentifier> townIdentifiers){
        townIdentifiers = Set.copyOf(townIdentifiers);
    }
    public static synchronized Set<TownIdentifier> townIdentifiers(){
        return Set.copyOf(townIdentifiers);
    }
    public Cache(String filePath) {
        this.filePath = filePath;
        load();
    }

    public String filePath() {
        return filePath;
    }

    public static <U extends Identifier> Cache<U> getInstance(String filePath){
        return new Cache<>(filePath);
    }
    @SuppressWarnings("unchecked")
    public void load()  {
        favoriteSet = new HashSet<>();
        ConfigManager manager = new ConfigManager();
        try {
            if (!manager.exists(filePath)) manager.write(filePath, null);
            JsonNode nodes = manager.read(filePath);
            for(JsonNode node : nodes) {
                String name = node.get("name").asText();
                String id = node.get("id").asText();
                Set<Identifier> identifiers = new HashSet<>();
                for (JsonNode object : node.get("objects")) {
                    identifiers.add(new ObjectMapper().enable(JsonParser.Feature.IGNORE_UNDEFINED).readerFor(Identifier.class).readValue(object.asText()));
                }
                favoriteSet.add(new Favorite<>(name, id, (Set<T>) identifiers));
            }
        }catch (IOException e){
            ChatUtil.sendException(e);
        }
    }
    public void removeFavorites(Favorite<T> favorite){
        favoriteSet.remove(favorite);
        try {
            new ConfigManager().write(filePath, favoriteSet);
        } catch (IOException e) {
            throw new  IllegalStateException(e);
        }
    }
    public boolean addFavorites(Favorite<T> favorite){
        if(!favoriteSet.contains(favorite)) favoriteSet.add(favorite);
        else {
            ChatUtil.sendWarning("don't repeat the addition");
            return false;
        }
        try {
            new ConfigManager().write(filePath, favoriteSet);
            return true;
        } catch (IOException e){
            throw new IllegalStateException(e);
        }
    }
    public Set<Favorite<T>> favoritesSet(){
        return favoriteSet;
    }
    public void save() throws IOException{
        new ConfigManager().write(filePath, favoriteSet);

    }
}
