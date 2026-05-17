package io.github.anaxolotldreamerr.client.cache;

import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Town;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TownCache {
    private Set<TownIdentifier> townIdentifiers = new HashSet<>();
    private Set<Town> towns = new HashSet<>();
    public TownCache(){}
    public TownCache addTownIdentifier(TownIdentifier... townIdentifiers){
        this.townIdentifiers.addAll(Arrays.asList(townIdentifiers));
        return this;
    }
    public TownCache addTown(Town... towns){
        this.towns.addAll(Arrays.asList(towns));
        return this;
    }
}
