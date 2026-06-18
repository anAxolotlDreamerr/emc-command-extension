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
import io.github.anaxolotldreamerr.client.model.Player;

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
    public Set<Identifier> filter(Set<Identifier> identifiers, PlayerIdentifier identifier) {
        if(!identifiers.isEmpty()) {
            Identifier temp = identifiers.iterator().next();
            if (temp instanceof TownIdentifier) {
                Player player = Cache.getPlayer(identifier);
                return Set.of(player.town());
            }
            if (temp instanceof NationIdentifier) {
                Player player = Cache.getPlayer(identifier);
                return Set.of(player.nation());
            }
            if(temp instanceof PlayerIdentifier){
                return Set.of(identifier);
            }
            throw new NullPointerException("the search argument:"+NAME+" does not support the type");
        }
        throw new NullPointerException("the favorites is empty");
    }

    @Override
    public Set<PlayerIdentifier> getAll() {
        return Cache.playerIdentifiers();
    }
    @Override
    public String name() {
        return NAME;
    }
    public static String getName(){
        return NAME;
    }
}
