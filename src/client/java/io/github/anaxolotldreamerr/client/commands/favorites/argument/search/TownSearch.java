package io.github.anaxolotldreamerr.client.commands.favorites.argument.search;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.NationType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;

import java.util.Set;

public class TownSearch implements SearchArgument<TownIdentifier> {
    private final static String NAME = "-t";
    @Override
    public Set<Identifier> lookup(TownIdentifier identifier, TypeArgument<? extends Identifier> type) {
        if(type instanceof TownType){
            return Set.of(identifier);
        }
        if(type instanceof NationType){
            return Set.of(Cache.getTown(identifier).nation());
        }
        if (type instanceof PlayerType){
            return Set.copyOf(Cache.getTown(identifier).residents());
        }
        throw new IllegalArgumentException("the search parameter -t does not support type:"+type);
    }

    @Override
    public Set<Identifier> filter(Set<TownIdentifier> identifiers, TypeArgument<? extends Identifier> type) {
        return Set.of();
    }

    @Override
    public Set<TownIdentifier> getAll(){
        return Cache.townIdentifiers();
    }
    public static String getName(){
        return NAME;
    }
}

