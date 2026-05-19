package io.github.anaxolotldreamerr.client.commands.favorites;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorites;
import io.github.anaxolotldreamerr.client.util.ChatUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public enum TypeArguments {
    TOWN("-t","favorites/town.json"){
        @Override
        public void load() throws IOException {
            ConfigManager manager = new ConfigManager();
            if(manager.exists(filePath))manager.write(filePath,null);
            favoritesSet =new ObjectMapper()
                    .readValue(manager
                            .read(filePath)
                            .asText(), new TypeReference<>() {});
        }
    };
    private final String ARGNAME;
    protected Set<Favorites> favoritesSet;
    protected final String filePath;
    TypeArguments(String argName,String filePath){
        this.ARGNAME = argName;
        this.filePath = filePath;
    }
    public void update(){
        try {
            new ConfigManager().write(filePath,favoritesSet);
        } catch (IOException e){
            ChatUtil.sendException(e);
        }
    }
    public void addFavorites(Favorites favorites){
        favoritesSet.add(favorites);
        try {
            new ConfigManager().write(filePath,favoritesSet);
        } catch (IOException e){
            ChatUtil.sendException(e);
        }
    }
    public void removeFavorites(Favorites favorites){
        favoritesSet.remove(favorites);
        try {
            new ConfigManager().write(filePath,favoritesSet);
        } catch (IOException e) {
            ChatUtil.sendException(e);
        }
    }
    protected abstract void load() throws IOException;
}
