package dev.radiance.ultivote.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import dev.radiance.ultivote.Main;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;


public class VoteListener implements Listener {
    Main plugin;
    public VoteListener(Main instance) {
        plugin = instance;
    }
    @EventHandler
    public void onVote(VotifierEvent event) {
        HashMap<String,PermissionAttachment> attachments = new HashMap<String, PermissionAttachment>();
        Vote vote = event.getVote();
        String username = vote.getUsername();
        String serviceName = vote.getServiceName();
        FileConfiguration config = plugin.getConfig();
        Player player = Bukkit.getPlayerExact(username);
        PermissionAttachment perm = player.addAttachment(plugin);

        if (username.length() < 1)
            return;

        if (player != null)  {
            // TODO PLEASE MAKE COMMAND LIST
            if(!config.getString("sound").equals("NONE"))
                player.playSound(player.getLocation(), Sound.valueOf(config.getString("sound")), 5F, 1F);
            if(!config.getString("effect").equals("NONE"))
                player.playEffect(player.getLocation(), Effect.valueOf(config.getString("effect")), 1);
        } else {
            if(!config.getString("offlinerewards").equals("false"))
                perm.setPermission("ultivote.rewards", true);
        }
    }
}
