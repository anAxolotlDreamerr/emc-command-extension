package io.github.anaxolotldreamerr.client.commands.favorites;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;

import java.util.Optional;
import java.util.function.Function;

public enum TypeArguments{
    TOWN("-t",
            "favorites/town.json"){
        private Cache<TownIdentifier> cache;
        @Override
        @SuppressWarnings("unchecked")
        public Cache<TownIdentifier> cache() {
            return cache;
        }
    };
    private final String ARGNAME;
    <T extends Identifier> TypeArguments(String argName, String filePath){
        this.ARGNAME = argName;
    }
    abstract <T extends Identifier> Cache<T> cache();
}
