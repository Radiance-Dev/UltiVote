package dev.radiance.ultivote.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import dev.radiance.ultivote.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;

public class VoteListener implements Listener {

    private final Main plugin;
    private final FileConfiguration config;

    public VoteListener(Main instance) {
        plugin = instance;
        config = plugin.getConfig();
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();

        String username = vote.getUsername();
        String serviceName = vote.getServiceName();
        Player player = Bukkit.getPlayerExact(username);

        if (username.length() < 1)
            return;

        //todo: figure out how to attach permission to offlinePlayers

        if (player == null || !player.isOnline()) {

            if (config.getBoolean("offlinerewards")) {
                PermissionAttachment perm = player.addAttachment(plugin);

                perm.setPermission("ultivote.rewards", true);
            }

            return;
        }

        if (!config.getString("sound.sound-effect").equals("NONE"))
            player.playSound(player.getLocation(),
                    Sound.valueOf(config.getString("sound.sound-effect")),
                    (float) config.get("sound.volume"),
                    (float) config.get("sound.pitch"));

        if (!config.getString("effect").equals("NONE"))
            player.playEffect(player.getLocation(), Effect.valueOf(config.getString("effect")), 1);

        if (!config.getString("broadcast").equals("NONE"))
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("broadcast")
                            .replace("{player}", username)));

        if (!config.getStringList("commands").isEmpty()) {
            List<String> commands = config.getStringList("commands");

            for (String command : commands)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", username));

        }

    }

}
