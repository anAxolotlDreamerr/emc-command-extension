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
            ,Config.HATRED_PLAYER_NAME_COLOR,Config::longHatredPlayerNameColor
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
            ,Config.HATRED_PLAYER_NAME_COLOR,Config::setHatredPlayerNameColor
    );
    private static final Map<String,BiFunction<Config,Integer,Integer>> SET_INT = Map.of(
            Config.BORDER_OPACITY,Config::setBorderOpacity
    );
    private static final Map<String, Function<Config,Set<String>>> SUGGESTIONS = Map.of(
            Config.BORDER_COLOR,config-> config.colors().keySet()
            ,Config.HATRED_PLAYER_NAME_COLOR, config->config.colors().keySet()
    );
    private static final Set<String> OPTIONS = Set.of(
            Config.HATRED_PLAYER_NAME_COLOR,
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

    public static Set<String> getSuggestions(String option){
        if(SUGGESTIONS.containsKey(option)) return SUGGESTIONS.get(option).apply(config);
        return Set.of();
    }

    public static String getString(String option) {
        if(GET_STRING.containsKey(option)) return GET_STRING.get(option).apply(config);
        throw new NullPointerException("No such option:"+option);
    }

    public static Long getLong(String option) {
        if(GET_LONG.containsKey(option)) return GET_LONG.get(option).apply(config);
        throw new NullPointerException("No such option:"+option);
    }

    public static Integer getInteger(String option){
        if(GET_INT.containsKey(option)) return GET_INT.get(option).apply(config);
        throw new NullPointerException("No such option:"+option);
    }

    public static Config getConfig(){
        return Config.copyOf(config);
    }

    public static void setString(String option,String value) throws IOException {
        if(!SET_STRING.containsKey(option)){
            throw new NullPointerException("No such option:"+option);
        }
        SET_STRING.get(option).apply(config,value);
        save();
    }

    public static void setLong(String option,String value) throws IOException {
        if(!SET_LONG.containsKey(option)){
            throw new NullPointerException("No such option:"+option);
        }
        SET_LONG.get(option).apply(config,value);
        save();
    }

    public static void setInteger(String option,String value) throws IOException {
        Integer v = Integer.parseInt(value);
        if(!SET_INT.containsKey(option)){
            throw new NullPointerException("No such option:"+option);
        }
        SET_INT.get(option).apply(config,v);
        save();
    }

    public static void save() throws IOException {
            ConfigManager.getInstance().write("config.json",config);
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

    public static void load() throws IOException {
        ConfigManager manager = ConfigManager.getInstance();
        if(DEFAULT_CONFIG == null){
            String json = readResource("default_config.json");
            DEFAULT_CONFIG = manager.mapper.readValue(json, Config.class);
        }
        JsonNode jsonNode;
        jsonNode = getInstance().read("config.json");
        config = Config.copyOf(DEFAULT_CONFIG);
        boolean needRepair = false;
        for(String option : options()) {
            if(jsonNode.hasNonNull(option)) {
                try {
                    set(option,jsonNode.get(option).asText());
                }catch (Exception e){
                    needRepair = true;
                }
            }else {
                needRepair = true;
            }
        }
        if(needRepair){
            save();
        }
    }

    public static void reset(String option) throws IOException {
        if(hasString(option)){
            setString(option,GET_STRING.get(option).apply(DEFAULT_CONFIG));
            return;
        }
        if(hasLong(option)){
            setLong(option,Long.toHexString(GET_LONG.get(option).apply(DEFAULT_CONFIG)));
            return;
        }
        if(hasInteger(option)){
            setInteger(option,Integer.toString(GET_INT.get(option).apply(DEFAULT_CONFIG)));
            return;
        }
        if(Config.COLORS.equals(option)){
            config.setColors(DEFAULT_CONFIG.colors());
            save();
            return;
        }
        throw new NullPointerException("No such option:"+option);
    }

    public static boolean hasInteger(String option){
        return GET_INT.containsKey(option);
    }

    public static boolean hasLong(String option){
        return GET_LONG.containsKey(option);
    }

    public static boolean hasString(String option){
        return GET_STRING.containsKey(option);
    }

    public static Set<String> options(){
        return Set.copyOf(OPTIONS);
    }

    public static void set(String option,String value) throws IllegalArgumentException, NullPointerException, IOException {
        if(hasInteger(option)){
            SET_INT.get(option).apply(config,Integer.parseInt(value));
            save();
            return;
        }
        if(hasLong(option)){
            SET_LONG.get(option).apply(config,value);
            save();
            return;
        }
        if(hasString(option)){
            SET_STRING.get(option).apply(config,value);
            save();
            return;
        }
        throw new NullPointerException("No such option:"+option);
    }

    public static String get(String option) throws IllegalArgumentException,NullPointerException{
        if(hasInteger(option)){
           return GET_INT.get(option).apply(config).toString();
        }
        if(hasLong(option)){
            return Long.toHexString(GET_LONG.get(option).apply(config));
        }
        if(hasString(option)){
            return GET_STRING.get(option).apply(config);
        }
        throw new NullPointerException("No such option:"+option);
    }
}
