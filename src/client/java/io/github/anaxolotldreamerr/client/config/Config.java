package io.github.anaxolotldreamerr.client.config;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Config {
    public static final String TOWN_URI = "townURI";
    public static final String NATION_URI = "nationURI";
    public static final String PLAYER_URI = "playerURI";
    public static final String BORDER_COLOR = "borderColor";
    public static final String BORDER_OPACITY = "borderOpacity";
    public static final String Hatred_Player_Name_Color = "hatredPlayerNameColor";
    public static final String COLORS = "colors";

    private static final String REGEX = "^[0-9a-fA-F]{8}$";
    private String townURI;
    private String nationURI;
    private String playerURI;

    private int borderOpacity;
    private Long borderColor;

    private Long hatredPlayerNameColor;

    private Map<String,Long> colors;

    public static Config copyOf(Config config){
        Config c = new Config();
        c.setTownURI(config.townURI);
        c.setNationURI(config.nationURI);
        c.setPlayerURI(config.playerURI);
        c.setBorderColor(config.borderColor);
        c.setBorderOpacity(config.borderOpacity);
        c.setColors(config.colors());
        return c;
    }

    @JsonGetter("townURI")
    public String townURI() {
        return townURI;
    }

    @JsonSetter("townURI")
    public String setTownURI(String townURI) {
      return  this.townURI = townURI;
    }

    @JsonGetter("nationURI")
    public String nationURI() {
        return nationURI;
    }

    @JsonSetter("nationURI")
    public String setNationURI(String nationURI) {
      return   this.nationURI = nationURI;
    }

    @JsonGetter("playerURI")
    public String playerURI() {
        return playerURI;
    }

    @JsonSetter("playerURI")
    public String setPlayerURI(String playerURI) {
      return   this.playerURI = playerURI;
    }

    @JsonGetter("BorderColor")
    public String borderColor() {
        String value = Long.toHexString(borderColor);
        return "0".repeat(8-value.length())+value;
    }

    public Long longBorderColor(){
        return borderColor;
    }

    @JsonSetter("BorderColor")
    public Long setBorderColor(String borderColor) {
        if(!borderColor.matches(REGEX) && !colors.containsKey(borderColor)) throw new IllegalArgumentException("Invalid borderColor hex value:"+borderColor);
        if(colors != null) {
            return this.borderColor = colors.containsKey(borderColor) ? colors.get(borderColor) : Long.parseLong(borderColor, 16);
        }
        return this.borderColor = Long.parseLong(borderColor, 16);
    }

    public Long setBorderColor(Long borderColor) {
        if( borderColor >=0L && borderColor<=0xFFFFFFFFL) return this.borderColor = borderColor;
        throw new IllegalArgumentException(Long.toHexString(borderColor)+" is out of the range:[0,0xFFFFFFFF]");
    }

    @JsonGetter("HatredPlayerNameColor")
    public String hatredPlayerNameColor() {
       String value = Long.toHexString(hatredPlayerNameColor);
        return "0".repeat(8-value.length())+value;
    }
    public Long longHatredPlayerNameColor(){
        return hatredPlayerNameColor;
    }

    @JsonSetter("HatredPlayerNameColor")
    public Long setHatredPlayerNameColor(String hatredPlayerNameColor) {
        if(!hatredPlayerNameColor.matches(REGEX) && !colors.containsKey(hatredPlayerNameColor)) throw new IllegalArgumentException("Invalid hatredPlayerNameColor hex value:"+hatredPlayerNameColor);
        if(colors != null) {
            return this.hatredPlayerNameColor = colors.containsKey(hatredPlayerNameColor) ? colors.get(hatredPlayerNameColor) : Long.parseLong(hatredPlayerNameColor, 16);
        }
        return this.hatredPlayerNameColor = Long.parseLong(hatredPlayerNameColor, 16);
    }

    public Long setHatredPlayerNameColor(Long hatredPlayerNameColor) {
        if( hatredPlayerNameColor >=0L && hatredPlayerNameColor<=0xFFFFFFFFL) return this.hatredPlayerNameColor = hatredPlayerNameColor;
        throw new IllegalArgumentException(Long.toHexString(hatredPlayerNameColor)+" is out of the range:[0,0xFFFFFFFF]");
    }

    @JsonGetter("colors")
    public Map<String,String> colors(){
        return colors == null ? Map.of() : colors.keySet().stream().collect(Collectors.toMap(Function.identity(),key -> {
            String value = Long.toHexString(colors.get(key));
            return "0".repeat(8-value.length())+value;
        }
        ));
    }

    public Map<String,Long> longColors(){
        return Map.copyOf(colors);
    }

    @JsonSetter("colors")
    public void setColors(Map<String,String> colors){
        this.colors = new HashMap<>();
        for(String key : colors.keySet()){
            String color = colors.get(key);
            if(!color.matches(REGEX)) throw new IllegalArgumentException("Invalid color hex value:"+color);
            Long c = Long.parseLong(color,16);
            this.colors.put(key,c);
        }
    }

    @JsonGetter("BorderOpacity")
    public int borderOpacity(){
        return borderOpacity;
    }
    @JsonSetter("BorderOpacity")
    public int setBorderOpacity(int borderOpacity){
        if(borderOpacity<=255 && borderOpacity>=0) return this.borderOpacity = borderOpacity;
        throw new IllegalArgumentException(borderOpacity+" is out of the range [0,255]");
    }

    public void normalize(Config template){
        if(townURI == null){
            townURI = template.townURI;
        }
        if(nationURI == null){
            nationURI = template.nationURI;
        }
        if(playerURI == null){
            playerURI = template.playerURI;
        }
        if(borderColor==null){
            borderColor = template.longBorderColor();
        }

       if(hatredPlayerNameColor==null){
           hatredPlayerNameColor = Long.valueOf(template.hatredPlayerNameColor(),16);
       }

       if(colors==null){
        colors =Map.copyOf(template.colors);
       }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Config config)) return false;
        return borderOpacity == config.borderOpacity && Objects.equals(townURI, config.townURI) && Objects.equals(nationURI, config.nationURI) && Objects.equals(playerURI, config.playerURI) && Objects.equals(borderColor, config.borderColor) && Objects.equals(hatredPlayerNameColor, config.hatredPlayerNameColor) && Objects.equals(colors, config.colors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(townURI, nationURI, playerURI, borderOpacity, borderColor, hatredPlayerNameColor, colors);
    }

    @Override
    public String toString() {
        return "Config{" +
                "townURI='" + townURI + '\'' +
                ", nationURI='" + nationURI + '\'' +
                ", playerURI='" + playerURI + '\'' +
                ", borderOpacity=" + borderOpacity +
                ", borderColor=" + borderColor +
                ", hatredPlayerNameColor=" + hatredPlayerNameColor +
                ", colors=" + colors +
                '}';
    }
}
