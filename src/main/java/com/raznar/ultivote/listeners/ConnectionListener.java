package com.raznar.ultivote.listeners;

import com.raznar.ultivote.Main;
import com.vexsoftware.votifier.model.Vote;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ConnectionListener implements Listener {

    private final Main plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();

        val lateRewards = plugin.getLateRewards();
        val playerRewards = lateRewards.stream()
                .filter((vote) -> vote.getUsername().equals(player.getName()))
                .collect(Collectors.toSet());

        for (Vote vote : playerRewards)
            plugin.giveVoteRewards(vote);
    }

}
