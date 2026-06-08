package io.github.anaxolotldreamerr.client.cache;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import io.github.anaxolotldreamerr.client.model.Nation;
import io.github.anaxolotldreamerr.client.model.Player;
import io.github.anaxolotldreamerr.client.model.Town;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Cache<T extends Identifier> {
    private Set<Favorite<T>> favoriteSet;
    private final String filePath;
    private static Set<TownIdentifier> townIdentifiers;
    private static Set<NationIdentifier> nationIdentifiers;
    private static Set<PlayerIdentifier> playerIdentifiers;
    private static volatile Map<TownIdentifier,Town> towns = new HashMap<>();
    private static volatile Map<NationIdentifier,Nation> nations = new HashMap<>();
    private static volatile Map<PlayerIdentifier,Player> players = new HashMap<>();
    private static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor();
    private static boolean isStarting = false;
    private static Date updateDate;
    private static boolean isOutput = false;
    public static Date updateDate(){
        return updateDate;
    }
    public static void start(){
        if(!isStarting) {
            isStarting=true;
            SCHEDULER.scheduleAtFixedRate(()->{
                update();
                if(isOutput)
                    ChatUtil.send(Component.literal("All identifiers has been updated").withStyle(ChatFormatting.BLUE));
                updateObjects();
                if (isOutput)
                    ChatUtil.send(Component.literal("All loaded objects has been updated").withStyle(ChatFormatting.BLUE));
            }, 0, 3, TimeUnit.MINUTES);
        }
    }
    public static void isOutput(boolean isOutput){
        Cache.isOutput = isOutput;
        if (isOutput)
            ChatUtil.send(Component.literal("Update prompt has been opened"));
        else
            ChatUtil.send(Component.literal("Update prompt has been closed"));
    }
    public static void update(){
        try {
            townIdentifiers = EMCApiRequest.getTownIdentifiers();
            nationIdentifiers = EMCApiRequest.getNationIdentifiers();
            playerIdentifiers = EMCApiRequest.getPlayerIdentifiers();
            updateDate = new Date(System.currentTimeMillis());
        } catch (Exception e) {
            ChatUtil.sendException(e);
            throw new RuntimeException(e);
        }
    }

    public synchronized static void updateObjects(){
        try {
            if(!towns.isEmpty()){
                Set<TownIdentifier> keys = towns.keySet();
                Set<Town> townSet = Town.byIdentifiers(keys);
                Map<String, Town> uuidToTown;
                Function<Town, String> g = Town::uuid;
                uuidToTown = townSet.stream().collect(Collectors.toMap(g, Function.identity()));
                towns = keys.stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                k -> uuidToTown.get(k.uuid())
                        ));
            }
            if(!nations.isEmpty()){
                Set<NationIdentifier> keys = nations.keySet();
                Set<Nation> set = Nation.byIdentifiers(keys);
                Map<String, Nation> uuidTo;
                Function<Nation, String> g = Nation::uuid;
                uuidTo = set.stream().collect(Collectors.toMap(g, Function.identity()));
                nations = keys.stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                k -> uuidTo.get(k.uuid())
                        ));
            }
            if(!players.isEmpty()){
                Set<PlayerIdentifier> keys = players.keySet();
                Set<Player> set = Player.byIdentifiers(keys);
                Map<String, Player> uuidTo;
                Function<Player, String> g = Player::uuid;
                uuidTo = set.stream().collect(Collectors.toMap(g, Function.identity()));
                players = keys.stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                k -> uuidTo.get(k.uuid())
                        ));
            }
        } catch (IOException e) {
            ChatUtil.sendException(e);
            throw new RuntimeException(e);
        }
    }
    public synchronized static Town getTown(TownIdentifier townIdentifier){
        if(towns.containsKey(townIdentifier)) return towns.get(townIdentifier);
        try {
            Town town = Town.byIdentifiers(Set.of(townIdentifier)).iterator().next();
            towns.put(townIdentifier,town);
            return town;
        } catch (IOException e) {
            throw new NullPointerException("can't find the town:"+townIdentifier.name());
        }
    }
    public synchronized static Set<Town> getAllTowns(Set<TownIdentifier> identifiers){
        Set<Town> set = new HashSet<>();
        Set<TownIdentifier> toLoad = identifiers.stream().filter(identifier -> {
            if(towns.containsKey(identifier)){
                set.add(towns.get(identifier));
                return false;
            }
            return true;
        }).collect(Collectors.toSet());
        try {
             Set<Town> loaded = Town.byIdentifiers(toLoad);
             set.addAll(loaded);
             Map<TownIdentifier,Town> loadedTowns = loaded.stream().collect(Collectors.toMap(Town::identifier,Function.identity()));
             towns.putAll(loadedTowns);
        } catch (IOException e) {
            throw new NullPointerException("some towns can't be found");
        }
        return set;
    }
    public synchronized static Nation getNation(NationIdentifier identifier){
        if(nations.containsKey(identifier)) return nations.get(identifier);
        try {
            Nation nation = Nation.byIdentifiers(Set.of(identifier)).iterator().next();
            nations.put(identifier,nation);
            return nation;
        } catch (IOException e) {
            throw new NullPointerException("can't find the nation:"+identifier.name());
        }
    }
    public synchronized static Set<Nation> getAllNations(Set<NationIdentifier> identifiers){
        Set<Nation> set = new HashSet<>();
        Set<NationIdentifier> toLoad = identifiers.stream().filter(identifier -> {
            if(nations.containsKey(identifier)){
                set.add(nations.get(identifier));
                return false;
            }
            return true;
        }).collect(Collectors.toSet());
        try {
            Set<Nation> loaded = Nation.byIdentifiers(toLoad);
            set.addAll(loaded);
            Map<NationIdentifier,Nation> loadedTowns = loaded.stream().collect(Collectors.toMap(Nation::identifier,Function.identity()));
            nations.putAll(loadedTowns);
        } catch (IOException e) {
            throw new NullPointerException("some nations can't be found");
        }
        return set;
    }
    public synchronized static Player getPlayer(PlayerIdentifier identifier){
        if(players.containsKey(identifier)) return players.get(identifier);
        try {
            Player player = Player.byIdentifiers(Set.of(identifier)).iterator().next();
            players.put(identifier,player);
            return player;
        } catch (IOException e) {
            throw new NullPointerException("can't find the player:"+identifier.name());
        }
    }
    public synchronized static Set<Player> getAllPlayers(Set<PlayerIdentifier> identifiers){
        Set<Player> set = new HashSet<>();
        Set<PlayerIdentifier> toLoad = identifiers.stream().filter(identifier -> {
            if(players.containsKey(identifier)){
                set.add(players.get(identifier));
                return false;
            }
            return true;
        }).collect(Collectors.toSet());
        try {
            Set<Player> loaded = Player.byIdentifiers(toLoad);
            set.addAll(loaded);
            Map<PlayerIdentifier,Player> loadedTowns = loaded.stream().collect(Collectors.toMap(Player::identifier,Function.identity()));
            players.putAll(loadedTowns);
        } catch (IOException e) {
            throw new NullPointerException("some nations can't be found");
        }
        return set;
    }
    public static Set<TownIdentifier> townIdentifiers(){
        return Set.copyOf(townIdentifiers);
    }
    public static Set<NationIdentifier> nationIdentifiers(){return Set.copyOf(nationIdentifiers);}
    public static Set<PlayerIdentifier> playerIdentifiers(){return Set.copyOf(playerIdentifiers);}
    public Cache(String filePath) {
        this.filePath = filePath;
        load();
    }

    public static Map<TownIdentifier, Town> towns() {
        return Map.copyOf(towns);
    }

    public static Map<NationIdentifier, Nation> nations() {
        return Map.copyOf(nations);
    }

    public static Map<PlayerIdentifier, Player> players() {
        return Map.copyOf(players);
    }

    public String filePath() {
        return filePath;
    }

    public static <U extends Identifier> Cache<U> getInstance(String filePath){
        return new Cache<>(filePath);
    }
    @SuppressWarnings("unchecked")
    public void load()  {
        favoriteSet = new HashSet<>();
        ConfigManager manager = new ConfigManager();
        try {
            if (!manager.exists(filePath)) manager.write(filePath, null);
            JsonNode nodes = manager.read(filePath);
            for(JsonNode node : nodes) {
                String name = node.get("name").asText();
                String id = node.get("id").asText();
                Set<Identifier> identifiers = new HashSet<>();
                for (JsonNode object : node.get("objects")) {
                    identifiers.add(new ObjectMapper().treeToValue(object, Identifier.class));
                }
                favoriteSet.add(new Favorite<>(name, id, (Set<T>) identifiers));
            }
        }catch (IOException e){
            ChatUtil.sendException(e);
        }
    }
    public void removeFavorites(Favorite<T> favorite){
        favoriteSet.remove(favorite);
        try {
            new ConfigManager().write(filePath, favoriteSet);
        } catch (IOException e) {
            throw new  IllegalStateException(e);
        }
    }
    public boolean addFavorites(Favorite<T> favorite){
        if(!favoriteSet.contains(favorite)) favoriteSet.add(favorite);
        else {
            ChatUtil.sendWarning("don't repeat the addition");
            return false;
        }
        try {
            new ConfigManager().write(filePath, favoriteSet);
            return true;
        } catch (IOException e){
            throw new IllegalStateException(e);
        }
    }
    public Set<Favorite<T>> favoritesSet(){
        return favoriteSet;
    }
    public void save() throws IOException{
        new ConfigManager().write(filePath, favoriteSet);

    }
}
