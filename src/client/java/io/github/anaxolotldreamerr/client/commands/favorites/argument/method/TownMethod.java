package io.github.anaxolotldreamerr.client.commands.favorites.argument.method;

import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;

import java.util.Set;

public class TownMethod implements MethodArgument<TownIdentifier>{
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
}
