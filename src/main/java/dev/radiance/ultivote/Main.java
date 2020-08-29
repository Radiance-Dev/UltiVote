package dev.radiance.ultivote;

import dev.radiance.ultivote.commands.Cmd;
import dev.radiance.ultivote.commands.Vote;
import dev.radiance.ultivote.configs.Config;
import dev.radiance.ultivote.listeners.VoteListener;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.print("");
        setupListener();
        setupConfig();
        getCommand("uv").setExecutor(new Cmd());
        getCommand("ultivote").setExecutor(new Cmd());
        getCommand("vote").setExecutor(new Vote());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void setupListener() {
        getServer().getPluginManager().registerEvents(new VoteListener(this), this);
    }
    public void setupConfig() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Config.setup();
        Config.get().addDefault("offlinerewards", "true");
        Config.get().addDefault("offlineoprewards", "false");
        Config.get().addDefault("sound", "BLOCK_ANVIL_LAND");
        Config.get().addDefault("effect", "SPELL");
        Config.get().options().header();

        Config.get().options().copyDefaults(true);
        Config.save();
    }
}