package dev.radiance.ultivote.configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private static File file;
    private static FileConfiguration config;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("UltiVote").getDataFolder(), "config.yml");

        if(!file.exists()) {
            try {
                Bukkit.getLogger().info("CONFIG DOESNT EXIST!, CREATING NEW ONE...");
                file.createNewFile();
            }catch (IOException e) {
                Bukkit.getLogger().info("Creating config...");
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration get() {
        return config;
    }
    public static void save() {
        try{
            config.save(file);
        }catch (IOException e) {
            Bukkit.getLogger().info("Couldn't save the config");
        }
    }
    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
