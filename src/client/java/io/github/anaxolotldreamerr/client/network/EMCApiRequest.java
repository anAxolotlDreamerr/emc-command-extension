package io.github.anaxolotldreamerr.client.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public final class EMCApiRequest {

    private static URI townURI;
    private static URI nationURI;
    private static URI playerURI;
    private static final Exception WRONG_TOWNURI =new Exception("Wrong town URI,check the profile config.json");
    private static final Exception WRONG_NATIONURI =new Exception("Wrong nation URI,check the profile config.json");
    private static final Exception WRONG_PLAYERURI =new Exception("Wrong player URI,check the profile config.json");
    private static final ConfigManager config = new ConfigManager();
    private static final HttpClient CLIENT =
            HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER =
            new ObjectMapper();

    public static URI townURI() {
        return townURI;
    }

    public static URI nationURI() {
        return nationURI;
    }

    public static URI playerURI() {
        return playerURI;
    }

    private EMCApiRequest() {}
    public static Set<PlayerIdentifier> getPlayerIdentifiers(){
        String players;
        try {
            players = request(playerURI,null);
            if(players == null){
                ChatUtil.sendException(new Exception("PlayerIdentifiers json are null!"));
                return Collections.EMPTY_SET;
            }
        } catch (IOException e) {
            ChatUtil.sendException(WRONG_PLAYERURI);
            return Collections.EMPTY_SET;
        }
        try {
            return MAPPER.readValue(players, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            ChatUtil.send(Component.literal("Can't deserialize the PlayerIdentifier").withStyle(ChatFormatting.RED));
            return Collections.EMPTY_SET;
        }
    }
    public static Set<NationIdentifier> getNationIdentifiers(){
        String nations;
        try {
            nations = request(nationURI,null);
            if(nations == null){
                ChatUtil.sendException(new Exception("NationIdentifiers json are null!"));
                return Collections.EMPTY_SET;
            }
        } catch (IOException e) {
            ChatUtil.sendException(WRONG_NATIONURI);
            return Collections.EMPTY_SET;
        }
        try {
            return MAPPER.readValue(nations, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            ChatUtil.send(Component.literal("Can't deserialize the NationIdentifier").withStyle(ChatFormatting.RED));
            return Collections.EMPTY_SET;
        }
    }
    public static Set<TownIdentifier> getTownIdentifiers(){
        String towns;
        try {
            towns = request(townURI,null);
            if(towns == null){
                ChatUtil.sendException(new IllegalArgumentException("TownIdentifiers json are null!"));
                return Collections.EMPTY_SET;
            }
        } catch (IOException e) {
            ChatUtil.sendException(WRONG_TOWNURI);
            return Collections.EMPTY_SET;
        }
        try {
            ChatUtil.sendWarning("send");
            Set<TownIdentifier> townIdentifiers = new HashSet<>();
            JsonNode json = MAPPER.readTree(towns);
            for(JsonNode node : json){
                townIdentifiers.add(new TownIdentifier(node.get("name").asText(),node.get("uuid").asText()));
            }
            return townIdentifiers;
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String request(URI URI,String post) throws IOException {
        if(URI == null){
            ChatUtil.sendException(new IllegalArgumentException("The URI is null!"));
            return null;
        }
        HttpRequest request;
        if(post!=null) {
            request = HttpRequest.newBuilder()
                    .uri(URI)
                    .POST(HttpRequest.BodyPublishers.ofString(post))
                    .build();
        }else {
            request = HttpRequest.newBuilder()
                    .uri(URI)
                    .GET()
                    .build();
        }
       HttpResponse<String> response;
       try {
           response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
       } catch (InterruptedException e) {
           ChatUtil.sendException(e);
           return null;
       }
        return response.body();
   }
   public static void configure(){
       JsonNode conf;
       if(!config.exists("config.json")) {
           try {
               config.write("config.json",new DefaultConfiguration());
           } catch (IOException e) {
               ChatUtil.sendException(e);
           }
       }
       try {
           conf = config.read("config.json");
       } catch (IOException e) {
           ChatUtil.sendException(e);
           try {
               config.write("config.json",new DefaultConfiguration());
           } catch (IOException ex) {
               ChatUtil.sendException(ex);
           }
           configure();
           return;
       }
       try {
           townURI = URI.create(conf.get("townURI").asText());
           nationURI = URI.create(conf.get("nationURI").asText());
           playerURI = URI.create(conf.get("playerURI").asText());
       }catch (NullPointerException e){
           try {
               config.write("config.json",new DefaultConfiguration());
           } catch (IOException ex) {
               ChatUtil.sendException(ex);
           }
           configure();
           ChatUtil.sendException(e);
       }
   }
   private static class DefaultConfiguration{
        public String townURI = "https://api.earthmc.net/v4/towns";
        public String nationURI = "https://api.earthmc.net/v4/nations";
        public String playerURI = "https://api.earthmc.net/v4/players";
   }
}