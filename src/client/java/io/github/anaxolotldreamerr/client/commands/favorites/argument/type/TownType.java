package io.github.anaxolotldreamerr.client.commands.favorites.argument.type;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;


public class TownType implements TypeArgument<TownIdentifier>{
    private final Cache<TownIdentifier> cache;
    private static final String NAME = "-t";
    private final static TownType TOWNTYPE = new TownType();
    private final static String filePath = "favorites/town.json";
    public TownType(){
        this.cache = Cache.getInstance(filePath);
    }
    public static String filePath(){
        return filePath;
    }
    public static TownType getInstance(){
        return TOWNTYPE;
    }
    public static String name(){
        return NAME;
    }
    @Override
    public Cache<TownIdentifier> cache() {
        return cache;
    }
}
