package dev.radiance.ultivote.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Messages {
    public static void helpMsg(Player player) {
        player.sendMessage("");
        player.sendMessage("");
    }
    public static void reloadMsg(Player player) {
        player.sendMessage("§aConfig has been reloaded!");
    }
    public static void versionMsg(Player player) {
        player.sendMessage("§aYou are using version" + Bukkit.getPluginManager().getPlugin("UltiVote").getDescription().getVersion());
    }
    public static String voteMsg = "";
}
