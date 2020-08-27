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


public class VoteListener implements Listener {
    Main plugin;
    public VoteListener(Main instance) {
        plugin = instance;
    }
    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();
        String username = vote.getUsername();
        String serviceName = vote.getServiceName();
        FileConfiguration config = plugin.getConfig();
        Player player = Bukkit.getPlayerExact(username);

        if (username.length() < 1)
            return;

        if (player != null)  {
            if(!config.getString("sound").equals("none"))
                player.playSound(player.getLocation(), Sound.valueOf(config.getString("sound")), 5F, 1F);
            if(!config.getString("effect").equals("none"))
                player.playEffect(player.getLocation(), Effect.valueOf(config.getString("effect")), 1);
        } else {

            if(!config.getString("offlinerewards").equals("true")) {
                return;
            } else {

            }
        }
    }
}
