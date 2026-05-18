package io.github.anaxolotldreamerr.client.commands.favorites;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorites;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public enum TypeArguments {
    TOWN("-t"){
        @Override
        public void load() throws IOException {
            favoritesSet =new ObjectMapper()
                    .readValue(new ConfigManager()
                            .read("favorites/town.json")
                            .asText(), new TypeReference<>() {});
        }
    };
    private final String ARGNAME;
    protected Set<Favorites> favoritesSet;
    TypeArguments(String argName){
        this.ARGNAME = argName;
    }
    abstract void load() throws IOException;
}
