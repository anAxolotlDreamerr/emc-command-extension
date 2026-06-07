package io.github.anaxolotldreamerr.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;
import io.github.anaxolotldreamerr.client.util.RequestUtil;

import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Town(String name,PlayerIdentifier mayor,Coordinate coordinate,NationIdentifier nation,String uuid,Set<PlayerIdentifier> residents,Integer balance) {
    @JsonCreator
    public Town(@JsonProperty("name") String name
            , @JsonProperty("mayor") PlayerIdentifier mayor
            , @JsonProperty("coordinate") Coordinate coordinate
            , @JsonProperty("nation") NationIdentifier nation
            , @JsonProperty("uuid") String uuid
            , @JsonProperty("residents") Set<PlayerIdentifier> residents
            , @JsonProperty("balance") Integer balance) {
        this.name =name;
        this.mayor = mayor;
        this.coordinate = coordinate;
        this.nation = nation;
        this.uuid =uuid;
        this.residents = residents == null ? Set.of() : Set.copyOf(residents);
        this.balance = balance;
    }

    private static Town create(JsonNode json){
        String name;
        PlayerIdentifier mayor;
        Coordinate coordinate;
        NationIdentifier nation;
        String uuid;
        Set<PlayerIdentifier> residents = new HashSet<>();
        int balance;
        name = json.get("name").asText();
        uuid = json.get("uuid").asText();
        {
            JsonNode node = json.get("mayor");
            mayor = new PlayerIdentifier(node.get("name").asText(),node.get("uuid").asText());
        }
        {
            JsonNode node = json.get("coordinates").get("homeBlock");
            coordinate = new Coordinate(node.get(0).asInt(),node.get(1).asInt());
        }
        {
            JsonNode node = json.get("nation");
            nation = new NationIdentifier(node.get("name").asText(),node.get("uuid").asText());
        }
        balance = json.get("stats").get("balance").asInt();
        {
            JsonNode res = json.get("residents");
            for(JsonNode resident:res){
                residents.add(new PlayerIdentifier(resident.get("name").asText(),resident.get("uuid").asText()));
            }
        }
        return new Town(name,mayor,coordinate,nation,uuid,residents,balance);
    }
    public static Set<Town> byIdentifiers(Set<TownIdentifier> identifiers) throws IOException {
        String echo = EMCApiRequest.request(EMCApiRequest.townURI(), RequestUtil.mix(identifiers));
        JsonNode nodes = new ObjectMapper().readTree(echo);
        Set<Town> towns = new HashSet<>();
        for(JsonNode node : nodes)towns.add(create(node));
        return towns;
    }
    public TownIdentifier identifier(){
        return new TownIdentifier(name,uuid);
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
}
