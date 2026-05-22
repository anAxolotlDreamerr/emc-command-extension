package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.model.Favorites;

import java.util.Set;

public class FavoritesUtil {
    public static Integer maxId(Set<? extends Favorites<?>> set){
        Integer max = 0;
        for(Favorites<?> favorites :set){
             if(favorites.id().matches("^[1-9]\\d*$")) {
                 Integer temp =Integer.decode(favorites.id());
                 max = Integer.max(temp,max);
             }
        }
        return max;
    }
}
