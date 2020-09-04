package com.raznar.ultivote;

import com.raznar.ultivote.commands.UltiVoteCMD;
import com.raznar.ultivote.commands.VoteCMD;
import com.raznar.ultivote.listeners.VoteListener;
import com.vexsoftware.votifier.model.Vote;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class Main extends JavaPlugin {

    @Getter private static Main instance;
    private final int latestConfigVersion = 1;
    private HashMap<Vote, String> data = new HashMap<Vote, String>();

    @Override
    public void onEnable() {
        instance = this;

        this.setupListener();
        this.setupCommands();
        this.setupConfig();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
    /**
     * setups the listeners
     */
    private void setupListener() {
        Bukkit.getPluginManager().registerEvents(new VoteListener(this), this);
    }

    /**
     * setups the commands
     */
    private void setupCommands() {
        this.getCommand("ultivote").setExecutor(new UltiVoteCMD(this));
        this.getCommand("vote").setExecutor(new VoteCMD(this));
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
        File configFile = new File(this.getDataFolder(), "config.yml");

        if (!configFile.exists())
            this.saveDefaultConfig();

        int currentConfigVersion = this.getConfig().getInt("config-version");

        // if they're using an older config.yml, tell them that its outdated
        // if they have the latest config or they're using a 'secret' code as their config-version, return
        if (currentConfigVersion != latestConfigVersion && currentConfigVersion != 6969)
            this.getLogger().severe("Config.yml is outdated. Please update your config.yml!");
    }

}