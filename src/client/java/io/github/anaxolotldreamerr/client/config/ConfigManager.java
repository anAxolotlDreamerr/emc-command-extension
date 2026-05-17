package io.github.anaxolotldreamerr.client.config;
import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private final static Path CONFIGDIR = FabricLoader.getInstance().getConfigDir().resolve("emc_command_extension");
    public ConfigManager(){
            try {
                Files.createDirectories(CONFIGDIR);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create config directory", e);
            }
    }
    public String readFile(String filePath) throws IOException {
        Path file = CONFIGDIR.resolve(filePath);
        if(Files.exists(file)) return Files.readString(file);
        throw new FileNotFoundException("File:"+file+" isn't exist!");
    }
    public void writeFile(String filePath){
        Path file = CONFIGDIR.resolve(filePath);
        Files.createDirectories(file.getParent());
    }
}
