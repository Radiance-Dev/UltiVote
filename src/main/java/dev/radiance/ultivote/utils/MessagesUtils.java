package dev.radiance.ultivote.utils;

import dev.radiance.ultivote.Main;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class MessagesUtils {

    public void sendHelpMessage(Player player) {
        player.sendMessage("");
        player.sendMessage("");
    }

    public void reloadMsg(Player player) {
        player.sendMessage("§aConfig has been reloaded!");
    }

    public void versionMsg(Player player) {
        player.sendMessage("§aYou are using version" + Main.getInstance().getDescription().getVersion());
    }

    public void sendVoteMessage(Player player) {
        player.sendMessage("");
    }

}
