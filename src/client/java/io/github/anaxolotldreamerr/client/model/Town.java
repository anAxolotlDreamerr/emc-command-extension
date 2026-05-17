package io.github.anaxolotldreamerr.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;

import java.util.Set;
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Town {
    private final String name;
    private final PlayerIdentifier mayor;
    private final Coordinate coordinate;
    private final NationIdentifier nation;
    private final String uuid;
    private final Set<PlayerIdentifier> residents;
    private final Integer balance;
    @JsonCreator
    public Town(@JsonProperty("name") String name,@JsonProperty("mayor") PlayerIdentifier mayor,@JsonProperty("coordinate") Coordinate coordinate,@JsonProperty("nation") NationIdentifier nation,@JsonProperty("uuid") String uuid,@JsonProperty("residents") Set<PlayerIdentifier> residents,@JsonProperty("balance") Integer balance) {
        this.name =name;
        this.mayor = mayor;
        this.coordinate = coordinate;
        this.nation = nation;
        this.uuid =uuid;
        this.residents = residents == null ? Set.of() : Set.copyOf(residents);
        this.balance = balance;
    }

    public Town byIdentifier(TownIdentifier identifier){

    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Town town)) return false;
        return uuid.equals(town.uuid);
    }

    @Override
    public int hashCode(){
        return uuid.hashCode();
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonGetter("mayor")
    public PlayerIdentifier getMayor() {
        return mayor;
    }

    @JsonGetter("coordinate")
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @JsonGetter("nation")
    public NationIdentifier getNation() {
        return nation;
    }

    @JsonGetter("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonGetter("residents")
    public Set<PlayerIdentifier> getResidents() {
        return residents;
    }

    @JsonGetter("balance")
    public Integer getBalance() {
        return balance;
    }

}
