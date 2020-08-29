package dev.radiance.ultivote;

import dev.radiance.ultivote.commands.UltiVoteCMD;
import dev.radiance.ultivote.commands.VoteCMD;
import dev.radiance.ultivote.listeners.VoteListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    private final int latestConfigVersion = 1;
    private File configFile;

    @Override
    public void onEnable() {
        instance = this;

        setupListener();
        setupCommands();
        setupConfig();

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * setups the listeners
     */
    private void setupListener() {
        getServer().getPluginManager().registerEvents(new VoteListener(this), this);
    }

    /**
     * setups the commands
     */
    private void setupCommands() {
        getCommand("ultivote").setExecutor(new UltiVoteCMD(this));
        getCommand("vote").setExecutor(new VoteCMD());
    }

    /**
     * setups the config.yml
     * this includes a config update checker
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void setupConfig() {
        if (!this.getDataFolder().exists())
            this.getDataFolder().mkdirs();

        // gets config.yml from plugins/UltiVote
        configFile = new File(this.getDataFolder(), "config.yml");

        // if it already exists
        if (configFile.exists()) {
            try {
                int currentConfigVersion = this.getConfig().getInt("config-version");

                // if they're using an older config.yml, tell them that its outdated
                // if they have the latest config or they're using a 'secret' code as their config-version, return
                if (currentConfigVersion != latestConfigVersion && currentConfigVersion != 6969)
                    this.getLogger().severe("Config.yml is outdated. Please update your config.yml!");

                return;

            } catch (Exception ignored) {

            }
        }

        this.copyConfig();
    }

    /**
     * overrides the original config
     * <p>
     * I separate this from the method above so that
     * you can use this to replace the config if it got corrupted.
     * Just change private to public or something...
     */
    private void copyConfig() {

        try (InputStream is = this.getConfigAsStream()) {
            if (is == null)
                throw new Exception("Failed to get resource stream!");

            Files.delete(configFile.toPath());
            Files.copy(is, configFile.toPath());

            this.getLogger().info("Config.yml has been renewed");
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.reloadConfig();

    }

    /**
     * gets the original config.yml
     */
    private InputStream getConfigAsStream() {
        return this.getClass().getClassLoader().getResourceAsStream("config.yml");
    }

}