package io.github.anaxolotldreamerr.client.commands.favorites.argument.query;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorites;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NameQuery implements QueryArgument {
    @Override
    public <T extends Identifier> Map<String, Favorites<T>> map(Cache<T> cache) {
        Set<Favorites<T>> set = cache.favoritesSet();
        Map<String,Favorites<T>> map = new HashMap<>();
        for(Favorites<T> favorites :set){
            map.put(favorites.name(),favorites);
        }
        return map;
    }
}
