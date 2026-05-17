package io.github.anaxolotldreamerr.client.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.model.Nation;
import io.github.anaxolotldreamerr.client.model.Player;
import io.github.anaxolotldreamerr.client.model.Town;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class EMCApiRequest {

    private static final URL TOWN_URL;
    private static final URL NATION_URL;
    private static final URL PLAYER_URL;

    static {
        try {
            TOWN_URL = new URL("https://api.earthmc.net/v4/towns/");
            NATION_URL =new URL("https://api.earthmc.net/v4/nations/");
            PLAYER_URL =new URL("https://api.earthmc.net/v4/players/");
        } catch (MalformedURLException e) {
            ClientPlayConnectionEvents.JOIN.register(
                    (handler, sender, client) -> {

                    }
        }
    }



    private static final HttpClient CLIENT =
            HttpClient.newHttpClient();

    private static final ObjectMapper MAPPER =
            new ObjectMapper();

    private EMCApiRequest() {}

   private String request(String URL,String post){
        return CLIENT.send(HttpRequest.newBuilder().build())
   }
}