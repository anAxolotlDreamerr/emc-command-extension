package io.github.anaxolotldreamerr.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;
import io.github.anaxolotldreamerr.client.util.RequestUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Nation(String name,PlayerIdentifier king,Coordinate coordinate,TownIdentifier capital,String uuid,Set<TownIdentifier> towns,Integer balance,Set<PlayerIdentifier> residents) {

    @JsonCreator
    public Nation(@JsonProperty("name") String name
            , @JsonProperty("king") PlayerIdentifier king
            , @JsonProperty("coordinate") Coordinate coordinate
            , @JsonProperty("capital") TownIdentifier capital
            , @JsonProperty("uuid") String uuid
            , @JsonProperty("towns") Set<TownIdentifier> towns
            , @JsonProperty("balance") Integer balance
            ,@JsonProperty("residents") Set<PlayerIdentifier> residents) {
        this.name =name;
        this.king = king;
        this.coordinate = coordinate;
        this.capital = capital;
        this.uuid =uuid;
        this.towns = towns == null ? Set.of() : Set.copyOf(towns);
        this.balance = balance;
        this.residents = residents == null ? Set.of() : Set.copyOf(residents);
    }

    private static Nation create(JsonNode json){
        String name;
        PlayerIdentifier king;
        Coordinate coordinate;
        TownIdentifier capital;
        String uuid;
        Set<TownIdentifier> towns = new HashSet<>();
        int balance;
        Set<PlayerIdentifier> residents = new HashSet<>();
        name = json.get("name").asText();
        uuid = json.get("uuid").asText();
        {
            JsonNode node = json.get("king");
            king = new PlayerIdentifier(node.get("name").asText(),node.get("uuid").asText());
        }
        {
            JsonNode node = json.get("coordinates").get("spawn");
            coordinate = new Coordinate(node.get("x").asInt(),node.get("z").asInt());
        }
        {
            JsonNode node = json.get("capital");
            capital = new TownIdentifier(node.get("name").asText(),node.get("uuid").asText());
        }
        balance = json.get("stats").get("balance").asInt();
        {
            JsonNode res = json.get("towns");
            for(JsonNode town:res){
                towns.add(new TownIdentifier(town.get("name").asText(),town.get("uuid").asText()));
            }
        }
        {
            JsonNode node = json.get("residents");
            for (JsonNode resident:node){
                residents.add(new PlayerIdentifier(resident.get("name").asText(),resident.get("uuid").asText()));
            }
        }
        return new Nation(name,king,coordinate,capital,uuid,towns,balance,residents);
    }
    public static Set<Nation> byIdentifiers(Set<NationIdentifier> identifiers) throws IOException {
        if(identifiers.isEmpty())return Set.of();
        String echo = EMCApiRequest.request(EMCApiRequest.nationURI(), RequestUtil.mix(identifiers));
        JsonNode nodes = new ObjectMapper().readTree(echo);
        Set<Nation> nations = new HashSet<>();
        for(JsonNode node : nodes)nations.add(create(node));
        return nations;
    }
    public NationIdentifier identifier(){
        return new NationIdentifier(name,uuid);
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Nation nation)) return false;
        return uuid.equals(nation.uuid);
    }

    @Override
    public int hashCode(){
        return uuid.hashCode();
    }
}
