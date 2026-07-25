package io.github.anaxolotldreamerr.client.config;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * 新增配置字段流程:
 *
 * 1. default_config.json 添加字段及默认值
 * 2. Config 添加字段变量
 * 3. Config 添加 getter/setter
 * 4. Config.copy() 添加字段复制
 * 5. 重新生成 equals/hashCode/toString
 * 6. ConfigManager.FEATURES 添加字段名
 * 7. ConfigManager 添加 getter/setter 映射
 * 8. ConfigManager.SUGGESTIONS 添加补全提示（可选）
 * 9. 对应 command toggle 添加字段
 */
public class Config {
    public static final String TOWN_URI = "TownURI";
    public static final String NATION_URI = "NationURI";
    public static final String PLAYER_URI = "PlayerURI";
    public static final String BORDER_COLOR = "BorderColor";
    public static final String BORDER_OPACITY = "BorderOpacity";
    public static final String HATRED_PLAYER_NAME_COLOR = "HatredPlayerNameColor";
    public static final String COLORS = "Colors";

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
        c.setHatredPlayerNameColor(config.hatredPlayerNameColor);
        return c;
    }

    @JsonGetter(TOWN_URI)
    public String townURI() {
        return townURI;
    }

    @JsonSetter(TOWN_URI)
    public String setTownURI(String townURI) {
      return  this.townURI = townURI;
    }

    @JsonGetter(NATION_URI)
    public String nationURI() {
        return nationURI;
    }

    @JsonSetter(NATION_URI)
    public String setNationURI(String nationURI) {
      return   this.nationURI = nationURI;
    }

    @JsonGetter(PLAYER_URI)
    public String playerURI() {
        return playerURI;
    }

    @JsonSetter(PLAYER_URI)
    public String setPlayerURI(String playerURI) {
      return   this.playerURI = playerURI;
    }

    @JsonGetter(BORDER_COLOR)
    public String borderColor() {
        String value = Long.toHexString(borderColor);
        return "0".repeat(8-value.length())+value;
    }

    public Long longBorderColor(){
        return borderColor;
    }

    @JsonSetter(BORDER_COLOR)
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

    @JsonGetter(HATRED_PLAYER_NAME_COLOR)
    public String hatredPlayerNameColor() {
       String value = Long.toHexString(hatredPlayerNameColor);
        return "0".repeat(8-value.length())+value;
    }
    public Long longHatredPlayerNameColor(){
        return hatredPlayerNameColor;
    }

    @JsonSetter(HATRED_PLAYER_NAME_COLOR)
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

    @JsonGetter(COLORS)
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

    @JsonSetter(COLORS)
    public void setColors(Map<String,String> colors){
        this.colors = new HashMap<>();
        for(String key : colors.keySet()){
            String color = colors.get(key);
            if(!color.matches(REGEX)) throw new IllegalArgumentException("Invalid color hex value:"+color);
            Long c = Long.parseLong(color,16);
            this.colors.put(key,c);
        }
    }

    @JsonGetter(BORDER_OPACITY)
    public int borderOpacity(){
        return borderOpacity;
    }
    @JsonSetter(BORDER_OPACITY)
    public int setBorderOpacity(int borderOpacity){
        if(borderOpacity<=255 && borderOpacity>=0) return this.borderOpacity = borderOpacity;
        throw new IllegalArgumentException(borderOpacity+" is out of the range [0,255]");
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
