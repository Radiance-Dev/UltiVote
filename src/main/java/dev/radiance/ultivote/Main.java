package dev.radiance.ultivote;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.vexsoftware.votifier.util.standalone.VoteReceiver;
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
        Config.get().options().copyDefaults(true);
        Config.save();
    }
}