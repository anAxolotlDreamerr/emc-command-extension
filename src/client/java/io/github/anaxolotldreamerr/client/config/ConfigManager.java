package io.github.anaxolotldreamerr.client.config;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConfigManager {
    private final static Path CONFIGDIR = FabricLoader.getInstance().getConfigDir().resolve("emc_command_extension");
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Map<String, Function<Config,String>> GET_STRING =Map.of(
            Config.TOWN_URI,Config::townURI
            ,Config.NATION_URI,Config::nationURI
            ,Config.PLAYER_URI,Config::playerURI
    );
    private static final Map<String,Function<Config,Long>> GET_LONG = Map.of(
            Config.BORDER_COLOR,Config::longBorderColor
            ,Config.Hatred_Player_Name_Color,Config::longHatredPlayerNameColor
    );
    private static final Map<String,Function<Config,Integer>> GET_INT = Map.of(
            Config.BORDER_OPACITY,Config::borderOpacity
    );
    private static final Map<String, BiFunction<Config,String,String>> SET_STRING = Map.of(
            Config.TOWN_URI,Config::setTownURI,
            Config.NATION_URI,Config::setNationURI,
            Config.PLAYER_URI,Config::setPlayerURI
    );
    private static final Map<String,BiFunction<Config,String,Long>> SET_LONG = Map.of(
            Config.BORDER_COLOR,Config::setBorderColor
            ,Config.Hatred_Player_Name_Color,Config::setHatredPlayerNameColor
    );
    private static final Map<String,BiFunction<Config,Integer,Integer>> SET_INT = Map.of(
            Config.BORDER_OPACITY,Config::setBorderOpacity
    );
    private static final Map<String, Function<Config,Set<String>>> SUGGESTIONS = Map.of(
            Config.BORDER_COLOR,config-> config.colors().keySet()
            ,Config.Hatred_Player_Name_Color,config->config.colors().keySet()
    );
    private static final Set<String> FEATURES = Set.of(
            Config.Hatred_Player_Name_Color,
            Config.BORDER_OPACITY,
            Config.PLAYER_URI,
            Config.NATION_URI,
            Config.COLORS,
            Config.BORDER_COLOR,
            Config.TOWN_URI
    );
    private static final ConfigManager INSTANCE = new ConfigManager();
    private static Config DEFAULT_CONFIG;
    private static Config config;

    public ConfigManager(){
        try {
            Files.createDirectories(CONFIGDIR);
        } catch (IOException e) {
            ChatUtil.sendException(new RuntimeException("Failed to create config directory", e));
        }
    }
    public static ConfigManager getInstance(){return INSTANCE;}
    public boolean exists(String filePath){
        Path file = CONFIGDIR.resolve(filePath);
        return Files.exists(file);
    }
    public JsonNode read(String filePath) throws IOException {
        Path file = CONFIGDIR.resolve(filePath);
        if(Files.exists(file)) return mapper.readTree(file.toFile());
        throw new FileNotFoundException("File:"+file+" isn't exist!");
    }
    public void write(String filePath,Object object) throws IOException {
        Path file = CONFIGDIR.resolve(filePath);
        if(!Files.exists(file)) {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
        }
        try {
            if (object != null) mapper
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .writerFor(object.getClass())
                    .writeValue(file.toFile(), object);
        }catch (IOException e){
            throw new IOException("Can't write object:"+object+" to the file:"+filePath);
        }
    }
    public void write(String filePath,String string) throws IOException{
        Path file = CONFIGDIR.resolve(filePath);
        if(!Files.exists(file)) {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
        }
        Files.writeString(file, string);
    }

    public static Set<String> getSuggestions(String feature){
        if(SUGGESTIONS.containsKey(feature)) return SUGGESTIONS.get(feature).apply(config);
        return Set.of();
    }

    public static String getString(String feature) {
        if(GET_STRING.containsKey(feature)) return GET_STRING.get(feature).apply(config);
        throw new NullPointerException("No such feature:"+feature);
    }
    public static Long getLong(String feature) {
        if(GET_LONG.containsKey(feature)) return GET_LONG.get(feature).apply(config);
        throw new NullPointerException("No such feature:"+feature);
    }
    public static Integer getInteger(String feature){
        if(GET_INT.containsKey(feature)) return GET_INT.get(feature).apply(config);
        throw new NullPointerException("No such feature:"+feature);
    }
    public static Config getConfig(){
        return Config.copyOf(config);
    }

    public static void setString(String feature,String value){
        if(!SET_STRING.containsKey(feature)){
            throw new NullPointerException("No such feature:"+feature);
        }
        SET_STRING.get(feature).apply(config,value);
        save();
    }
    public static void setLong(String feature,String value){
        if(!SET_LONG.containsKey(feature)){
            throw new NullPointerException("No such feature:"+feature);
        }
        SET_LONG.get(feature).apply(config,value);
        save();

    }
    public static void setInteger(String feature,String value){
        Integer v = Integer.parseInt(value);
        if(!SET_INT.containsKey(feature)){
            throw new NullPointerException("No such feature:"+feature);
        }
        SET_INT.get(feature).apply(config,v);
        save();
    }
    public static void save(){
        try {
            normalize();
            ConfigManager.getInstance().write("config.json",config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void reset(){
        try {
            config  = Config.copyOf(DEFAULT_CONFIG);
            ConfigManager.getInstance().write("config.json",DEFAULT_CONFIG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String readResource(String value){
        String config = null;
        try(InputStream stream = Config.class.getClassLoader()
                .getResourceAsStream(value)) {
            byte[] bytes;
            if (stream != null) {
                bytes = stream.readAllBytes();
                config = new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    public static void load(){
        ConfigManager manager = ConfigManager.getInstance();
        if(DEFAULT_CONFIG == null){
            String json = readResource("default_config.json");
            try {
                DEFAULT_CONFIG = manager.mapper.readValue(json, Config.class);
            } catch (Exception e) {
            }
            if (!manager.exists("config.json")) {
                try {
                    manager.write("config.json", json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            config =manager.mapper.treeToValue(manager.read("config.json"),Config.class);
            normalize();
        } catch (Exception e) {
            reset();
        }
    }

    public static void reset(String feature){
        if(hasString(feature)){
            setString(feature,GET_STRING.get(feature).apply(DEFAULT_CONFIG));
            return;
        }
        if(hasLong(feature)){
            setLong(feature,Long.toHexString(GET_LONG.get(feature).apply(DEFAULT_CONFIG)));
            return;
        }
        if(hasInteger(feature)){
            setInteger(feature,Integer.toString(GET_INT.get(feature).apply(DEFAULT_CONFIG)));
            return;
        }
        if(Config.COLORS.equals(feature)){
            config.setColors(DEFAULT_CONFIG.colors());
            save();
            return;
        }
        throw new NullPointerException("No such feature:"+feature);
    }

    public static boolean hasInteger(String feature){
        return GET_INT.containsKey(feature);
    }
    public static boolean hasLong(String feature){
        return GET_LONG.containsKey(feature);
    }
    public static boolean hasString(String feature){
        return GET_STRING.containsKey(feature);
    }

    public static Set<String> features(){
        return Set.copyOf(FEATURES);
    }

    private static void normalize(){
        config.normalize(DEFAULT_CONFIG);
    }
}
