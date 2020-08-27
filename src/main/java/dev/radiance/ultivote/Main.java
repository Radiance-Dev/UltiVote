package dev.radiance.ultivote;

import dev.radiance.ultivote.listeners.VoteListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.print("");
        setupListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void setupListener() {
        getServer().getPluginManager().registerEvents(new VoteListener(this), this);
    }
}
