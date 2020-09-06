package com.raznar.ultivote.listeners;

import com.raznar.ultivote.Main;
import com.raznar.ultivote.utils.Utils;
import com.vexsoftware.votifier.model.VotifierEvent;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class VoteListener implements Listener {

    private final Main plugin;

    @EventHandler
    public void onVote(VotifierEvent event) {
        val config = plugin.getConfig();
        val vote = event.getVote();
        val username = vote.getUsername();

        if (!Utils.isMinecraftName(username))
            return;

        val broadcastMessages = config.getStringList("on-player-vote.broadcast");
        if (!broadcastMessages.isEmpty()) {
            val fullBroadcastMessage = Utils.colorize(String.join("\n", broadcastMessages))
                    .replace("{service}", vote.getServiceName());

            Bukkit.broadcastMessage(fullBroadcastMessage);
        }

        plugin.increaseVoteCount();
        plugin.giveVoteRewards(vote);
    }

}
