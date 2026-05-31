package io.github.anaxolotldreamerr.client.commands.favorites.argument.search;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;

import java.util.Set;

public class TownSearch implements SearchArgument<TownIdentifier> {
    private final static String NAME = "-t";
    @Override
    public Set<TownIdentifier> lookup(Identifier identifier) {
        if(identifier instanceof TownIdentifier town){
            return Set.of(town);
        }
        throw new IllegalArgumentException("Can't deal with "+identifier.name());
    }

    @Override
    public Set<TownIdentifier> filter(Set<Identifier> identifiers) {
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
