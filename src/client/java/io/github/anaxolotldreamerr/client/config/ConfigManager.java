package io.github.anaxolotldreamerr.client.config;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private final static Path CONFIGDIR = FabricLoader.getInstance().getConfigDir().resolve("emc_command_extension");
    private final ObjectMapper mapper = new ObjectMapper();
    public ConfigManager(){
            try {
                Files.createDirectories(CONFIGDIR);
            } catch (IOException e) {
                ChatUtil.sendException(new RuntimeException("Failed to create config directory", e));
            }
    }
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
}
