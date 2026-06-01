package io.github.anaxolotldreamerr.client.commands.favorites.argument.type;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;

public class PlayerType implements TypeArgument<PlayerIdentifier> {
    private final Cache<PlayerIdentifier> cache;
    private static final String NAME = "-r";
    private final static TownType TOWNTYPE = new TownType();
    private final static String filePath = "favorites/player.json";
    public PlayerType(){
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
    public Cache<PlayerIdentifier> cache() {
        return cache;
    }
}
