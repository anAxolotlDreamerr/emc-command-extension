package io.github.anaxolotldreamerr.client.commands.favorites.argument.search;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.NationType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;

import java.util.Set;

public class PlayerSearch implements SearchArgument<PlayerIdentifier> {
    private final static String NAME = "-p";
    @Override
    public Set<Identifier> lookup(PlayerIdentifier identifier, TypeArgument<? extends Identifier> type) {
        if(type instanceof NationType){
            return Set.of(Cache.getPlayer(identifier).nation());
        }
        if(type instanceof PlayerType){
            return Set.of(identifier);
        }
        if(type instanceof TownType){
            return Set.of(Cache.getPlayer(identifier).town());
        }
        throw new NullPointerException("the search parameter -n does not support type:"+type);
    }

    @Override
    public Set<Identifier> filter(Set<PlayerIdentifier> identifiers, TypeArgument<? extends Identifier> type) {
        return Set.of();
    }

    @Override
    public Set<PlayerIdentifier> getAll() {
        return Cache.playerIdentifiers();
    }
    public static String getName(){
        return NAME;
    }
}
