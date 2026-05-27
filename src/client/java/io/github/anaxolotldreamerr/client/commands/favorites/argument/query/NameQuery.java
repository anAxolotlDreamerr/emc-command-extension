package io.github.anaxolotldreamerr.client.commands.favorites.argument.query;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NameQuery implements QueryArgument {
    private static final String NAME = "-N";
    @Override
    public <T extends Identifier> Map<String, Favorite<T>> map(Cache<T> cache) {
        Set<Favorite<T>> set = cache.favoritesSet();
        Map<String, Favorite<T>> map = new HashMap<>();
        for(Favorite<T> favorite :set){
            map.put(favorite.name(), favorite);
        }
        return map;
    }
    public static String getName(){
        return NAME;
    }
}
