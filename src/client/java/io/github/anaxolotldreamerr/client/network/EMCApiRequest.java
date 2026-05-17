package io.github.anaxolotldreamerr.client.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anaxolotldreamerr.client.exception.ExceptionRegistry;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class EMCApiRequest {

    private static URI townURI;
    private static URI nationURI;
    private static URI playerURI;
    private static final Exception WRONG_TOWNURI =new Exception("[ECE]Wrong town URI,check the profile config.json");
    private static final Exception WRONG_NATIONURI =new Exception("[ECE]Wrong nation URI,check the profile config.json");
    private static final Exception WRONG_PLAYERURI =new Exception("[ECE]Wrong player URI,check the profile config.json");

    private static final HttpClient CLIENT =
            HttpClient.newHttpClient();

    private static final ObjectMapper MAPPER =
            new ObjectMapper();

    private EMCApiRequest() {}

   private String request(URI URI,String post){
       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI)
               .POST(HttpRequest.BodyPublishers.ofString(post))
               .GET()
               .build();

   }
   public void reConnect(){
       townURI = URI.create("https://api.earthmc.net/v4/towns/");
       nationURI =URI.create("https://api.earthmc.net/v4/nations/");
           playerURI =URI.create("https://api.earthmc.net/v4/players/");
   }
}