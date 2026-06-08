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
import io.github.anaxolotldreamerr.client.model.Player;
import io.github.anaxolotldreamerr.client.model.Town;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Set<Identifier> filter(Set<Identifier> identifiers,TownIdentifier identifier) {
        if(!identifiers.isEmpty()) {
            Identifier temp = identifiers.iterator().next();
            if (temp instanceof TownIdentifier) {
                return Set.of(identifier);
            }
            if (temp instanceof NationIdentifier) {
                Town town = Cache.getTown(identifier);
                return Set.of(town.nation());
                /*Set<Nation> objects = Cache.getAllNations(identifiers.stream().map(NationIdentifier::byIdentifier).collect(Collectors.toSet()));
                Map<String,Nation> nameTo = objects.stream().collect(Collectors.toMap(Nation::name, Function.identity()));
                Map<Nation,Identifier> result = identifiers.stream().collect(Collectors.toMap(i->nameTo.get(i.name()),Function.identity()));
                return result.keySet().stream().filter(nation -> nation.towns().contains(identifier)).map(result::get).collect(Collectors.toSet());
                */
            }
            if(temp instanceof PlayerIdentifier){
                Town town = Cache.getTown(identifier);
                return Set.copyOf(town.residents());
                 }
            throw new NullPointerException("the search argument:"+NAME+" does not support the type");
        }
        throw new NullPointerException("the favorites is empty");
    }

    @Override
    public Set<TownIdentifier> getAll(){
        return Cache.townIdentifiers();
    }
    public static String getName(){
        return NAME;
    }
}

