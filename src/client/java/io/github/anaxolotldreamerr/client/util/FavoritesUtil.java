package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.model.Favorite;

import java.util.Set;

public class FavoritesUtil {
    public static Integer maxId(Set<? extends Favorite<?>> set){
        Integer max = 0;
        for(Favorite<?> favorite :set){
             if(favorite.id().matches("^[1-9]\\d*$")) {
                 Integer temp =Integer.decode(favorite.id());
                 max = Integer.max(temp,max);
             }
        }
        return max;
    }
}
