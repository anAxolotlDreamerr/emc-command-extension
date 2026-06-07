package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;

import java.util.Map;

public class QueryUntil {
    public static Favorite<Identifier> get(Map<String,Favorite<Identifier>> map,String key){
        if(map.containsKey(key)){
            return map.get(key);
        }
        throw new NullPointerException("unknow favorite:"+key);
    }
}
