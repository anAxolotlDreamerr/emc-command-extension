package io.github.anaxolotldreamerr.client.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.cache.TownCache;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.exception.ExceptionRegistry;
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

/*


 */
public final class EMCApiRequest {
    private static URI townURI;
    private static URI nationURI;
    private static URI playerURI;
    private static final Exception WRONG_TOWNURI =new Exception("[ECE]Wrong town URI,check the profile config.json");
    private static final Exception WRONG_NATIONURI =new Exception("[ECE]Wrong nation URI,check the profile config.json");
    private static final Exception WRONG_PLAYERURI =new Exception("[ECE]Wrong player URI,check the profile config.json");
    private static ConfigManager config = new ConfigManager();
    private static final HttpClient CLIENT =
            HttpClient.newHttpClient();

    private static final ObjectMapper MAPPER =
            new ObjectMapper();
    private EMCApiRequest() {}

    public static Set<TownIdentifier> getTownIdentifiers(){
        String towns;
        try {
            towns = request(townURI,"");
            ExceptionRegistry.turnOff(WRONG_TOWNURI);
        } catch (IOException e) {
            ExceptionRegistry.turnOn(WRONG_TOWNURI);
            return Collections.EMPTY_SET;
        }
        try {
            return MAPPER.readValue(towns, new HashSet<TownIdentifier>().getClass());
        } catch (JsonProcessingException e) {
            ChatUtil.send(Component.literal("[ECE]Can't deserialize the TownIdentifier").withStyle(ChatFormatting.RED));
            return Collections.EMPTY_SET;
        }
    }
    private static String request(URI URI,String post) throws IOException {
       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI)
               .POST(HttpRequest.BodyPublishers.ofString(post))
               .build();
       HttpResponse<String> response;
       try {
           response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
       } catch (InterruptedException e) {
           ChatUtil.send(Component.literal(e.getMessage()).withStyle(ChatFormatting.RED));
           return "";
       }
        return response.body();
   }
   public static void readConfig(){
       JsonNode conf;
       try {
           conf = config.read("config.json");
       } catch (IOException e) {
           ExceptionRegistry.turnOn(e);
           return;
       }
       townURI = URI.create(conf.get("townURI").asText());
       nationURI = URI.create(conf.get("nationURI").asText());
       playerURI = URI.create(conf.get("playerURI").asText());
   }
}