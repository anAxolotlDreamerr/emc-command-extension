package io.github.anaxolotldreamerr.client.commands.favorites.argument.search;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.NationType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Nation;
import io.github.anaxolotldreamerr.client.model.Town;

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
    public Set<Identifier> filter(Set<Identifier> identifiers, NationIdentifier identifier) {
        if(!identifiers.isEmpty()) {
            Identifier temp = identifiers.iterator().next();
            if (temp instanceof TownIdentifier) {
                Nation nation = Cache.getNation(identifier);
                return Set.copyOf(nation.towns());
            }
            if (temp instanceof NationIdentifier) {
                return Set.of(identifier);
            }
            if(temp instanceof PlayerIdentifier){
                Nation nation = Cache.getNation(identifier);
                return Set.copyOf(nation.residents());
            }
            throw new NullPointerException("the search argument:"+NAME+" does not support the type");
        }
        throw new NullPointerException("the favorites is empty");
    }

    @Override
    public Set<NationIdentifier> getAll() {
        return Cache.nationIdentifiers();
    }
    public static String getName(){
        return NAME;
    }
}
