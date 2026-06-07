package io.github.anaxolotldreamerr.client.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;
import io.github.anaxolotldreamerr.client.util.RequestUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
public record Player(String name
        , String uuid
        , TownIdentifier town
        , NationIdentifier nation
        , Integer balance
        , Set<PlayerIdentifier> friends) {
    private static Player create(JsonNode json){
        String name;
        String uuid;
        TownIdentifier town;
        NationIdentifier nation;
        int balance;
        Set<PlayerIdentifier> friends = new HashSet<>();
        name = json.get("name").asText();
        uuid = json.get("uuid").asText();
        {
            JsonNode node = json.get("town");
            town = new TownIdentifier(node.get("name").asText(),node.get("uuid").asText());
        }
        {
            JsonNode node = json.get("nation");
            nation = new NationIdentifier(node.get("name").asText(),node.get("uuid").asText());
        }
        balance = json.get("stats").get("balance").asInt();
        {
            JsonNode fr = json.get("friends");
            for(JsonNode friend:fr){
                friends.add(new PlayerIdentifier(friend.get("name").asText(),friend.get("uuid").asText()));
            }
        }
        return new Player(name,uuid,town,nation,balance,friends);
    }
    public static Set<Player> byIdentifiers(Set<PlayerIdentifier> identifiers) throws IOException {
        String echo = EMCApiRequest.request(EMCApiRequest.playerURI(), RequestUtil.mix(identifiers));
        JsonNode nodes = new ObjectMapper().readTree(echo);
        Set<Player> players = new HashSet<>();
        for(JsonNode node : nodes)players.add(create(node));
        return players;
    }
    public PlayerIdentifier identifier(){
        return new PlayerIdentifier(name,uuid);
    }
    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(!(obj instanceof Player player))
            return false;
        return uuid.equals(player.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
