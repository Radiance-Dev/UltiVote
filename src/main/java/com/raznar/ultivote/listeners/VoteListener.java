package com.raznar.ultivote.listeners;

import com.raznar.ultivote.Main;
import com.raznar.ultivote.utils.Utils;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.List;

public class VoteListener implements Listener {
    // It should be this right? (Im still learning hashmap)
    private HashMap<String, Player> data = new HashMap<String, Player>();
    private final Main plugin;

    public VoteListener(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onVote(VotifierEvent event) {
        FileConfiguration config = plugin.getConfig();
        Vote vote = event.getVote();

        String username = vote.getUsername();
        String serviceName = vote.getServiceName();
        Player player = Bukkit.getPlayerExact(username);
        ConfigurationSection section = config.getConfigurationSection("on-player-vote");

        if (username.length() < 1)
            return;

        data.put(player.getUniqueId().toString(), player);
        List<String> broadcastMessages = section.getStringList("broadcast");
        if (!broadcastMessages.isEmpty()) {
            String fullBroadcastMessage = Utils.colorize(String.join("\n", broadcastMessages));
            if(broadcastMessages.contains("{service}")) {
                for(String services : broadcastMessages)
                    Bukkit.broadcastMessage(Utils.colorize(String.join("\n", broadcastMessages.toString(), services.replace("{service}", serviceName))));
            } else {
                Bukkit.broadcastMessage(fullBroadcastMessage);
            }
        }
        while(!player.isOnline())
            config.getConfigurationSection("data").getKeys(false).forEach(key -> {
                Player players = (Player) config.get("data." + key);
                plugin.saveConfig();
            });
        if(!player.isOnline())
            return;
        data.remove(player.getUniqueId().toString(), player);
        String soundEffect = section.getString("sound.sound-effect");
        if (!soundEffect.isEmpty() && !soundEffect.equals("NONE"))
            player.playSound(player.getLocation(),
                    Sound.valueOf(soundEffect),
                    (float) section.get("sound.volume"),
                    (float) section.get("sound.pitch")
            );

        String effect = section.getString("effect");
        if (!effect.isEmpty() && !effect.equals("NONE"))
            player.playEffect(player.getLocation(), Effect.valueOf(effect), 1);


        List<String> executableCommands = section.getStringList("commands");
        if (!executableCommands.isEmpty()) {
            for (String command : executableCommands)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", username));
        }
    }
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("on-player-vote");
        String soundEffect = section.getString("sound.sound-effect");
        if(!data.containsKey(player.getUniqueId().toString()))
            return;

        data.remove(player.getUniqueId().toString());
        if (!soundEffect.isEmpty() && !soundEffect.equals("NONE"))
            player.playSound(player.getLocation(),
                    Sound.valueOf(soundEffect),
                    (float) section.get("sound.volume"),
                    (float) section.get("sound.pitch")
            );

        String effect = section.getString("effect");
        if (!effect.isEmpty() && !effect.equals("NONE"))
            player.playEffect(player.getLocation(), Effect.valueOf(effect), 1);


        List<String> executableCommands = section.getStringList("commands");
        if (!executableCommands.isEmpty()) {
            for (String command : executableCommands)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getDisplayName()));
        }
    }
}
