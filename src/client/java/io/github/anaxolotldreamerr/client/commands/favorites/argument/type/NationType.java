package io.github.anaxolotldreamerr.client.commands.favorites.argument.type;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;

public class NationType implements TypeArgument<NationIdentifier>{

    private final Cache<NationIdentifier> cache;
    private static final String NAME = "-n";
    private final static NationType NATIONTYPE = new NationType();
    private final static String filePath = "favorites/nation.json";
    public NationType(){
        this.cache = Cache.getInstance(filePath);
    }
    public static String filePath(){
        return filePath;
    }
    public static NationType getInstance(){
        return NATIONTYPE;
    }
    public static String name(){
        return NAME;
    }
    @Override
    public Cache<NationIdentifier> cache() {
        return cache;
    }
    @Override
    public String toString() {
        return NAME;
    }
}
