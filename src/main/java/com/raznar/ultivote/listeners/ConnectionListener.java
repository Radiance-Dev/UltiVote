package com.raznar.ultivote.listeners;

import com.raznar.ultivote.Main;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class ConnectionListener implements Listener {

    private final Main plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();

        // todo: give vote rewards to the player if they supposed to have one
    }

}
