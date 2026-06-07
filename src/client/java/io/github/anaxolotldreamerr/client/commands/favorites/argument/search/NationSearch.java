package io.github.anaxolotldreamerr.client.commands.favorites.argument.search;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.NationType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;

import java.util.HashSet;
import java.util.Set;

public class NationSearch implements SearchArgument<NationIdentifier>{
    private final static String NAME = "-n";
    @Override
    public Set<Identifier> lookup(NationIdentifier identifiers, TypeArgument<? extends Identifier> type) {
        if(type instanceof NationType){
            return Set.of(identifiers);
        }
        if(type instanceof PlayerType){
            return Set.copyOf(Cache.getNation(identifiers).residents());
        }
        if(type instanceof TownType){
            return Set.copyOf(Cache.getNation(identifiers).towns());
        }
        throw new NullPointerException("the search parameter -n does not support type:"+type);
    }

    @Override
    public Set<Identifier> filter(Set<NationIdentifier> identifiers, TypeArgument<? extends Identifier> type) {
        return Set.of();
    }

    @Override
    public Set<NationIdentifier> getAll() {
        return Cache.nationIdentifiers();
    }
    public static String getName(){
        return NAME;
    }
}
