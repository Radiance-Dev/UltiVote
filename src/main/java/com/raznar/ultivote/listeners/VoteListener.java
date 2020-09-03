package com.raznar.ultivote.listeners;

import com.raznar.ultivote.Main;
import com.raznar.ultivote.utils.Utils;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;

@RequiredArgsConstructor
public class VoteListener implements Listener {

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

        if (username.length() < 1)
            return;

        // todo: figure out how to attach permission to offlinePlayers
        // fixme: this will fail btw
        if (player == null || !player.isOnline()) {
            if (config.getBoolean("offlinerewards")) {
                PermissionAttachment perm = player.addAttachment(plugin);
                perm.setPermission("ultivote.rewards", true);
            }
            return;
        }

        ConfigurationSection section = config.getConfigurationSection("on-player-vote");

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

        List<String> executableCommands = section.getStringList("commands");
        if (!executableCommands.isEmpty()) {
            for (String command : executableCommands)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", username));
        }

    }

}
