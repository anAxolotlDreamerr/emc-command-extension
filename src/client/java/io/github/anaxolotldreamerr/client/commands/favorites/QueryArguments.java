package io.github.anaxolotldreamerr.client.commands.favorites;

import io.github.anaxolotldreamerr.client.model.Favorites;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum QueryArguments {
    NAME("-n"){
        @Override
        public Map<String,Favorites> query(TypeArguments type){
            Set<Favorites> set = type.favoritesSet;
            Map<String,Favorites> map = new HashMap<>();
            for(Favorites favorites :set){
                map.put(favorites.name(),favorites);
            }
            return map;
        }
    },ID("-i"){
        @Override
        public Map<String,Favorites> query(TypeArguments type) {
            Set<Favorites> set = type.favoritesSet;
            Map<String, Favorites> map = new HashMap<>();
            for (Favorites favorites : set) {
                map.put(favorites.id(), favorites);
            }
            return map;
            }
        };
    private final String ARG;
    QueryArguments(String ARG){
        this.ARG = ARG;
    }
    protected abstract Map<String, Favorites> query(TypeArguments type);
    public static QueryArguments defaultArg(){
        return NAME;
    }
}
