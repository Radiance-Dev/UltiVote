package com.raznar.ultivote.commands;

import com.raznar.ultivote.utils.BetterCommand;
import com.raznar.ultivote.Main;
import com.raznar.ultivote.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UltiVoteCMD extends BetterCommand {

    private final Main plugin;

    public UltiVoteCMD(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to execute this command!");
            return;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("ulti.vote")) {
            this.execute(sender, new String[]{"version"});
            return;
        }

        if (args.length < 1) {
            this.execute(sender, new String[]{"help"});
            return;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
            case "rl": {
                plugin.reloadConfig();
                player.sendMessage("§aConfig has been reloaded!");
                break;
            }
            case "rewards":
            case "reward": {

            }
            case "version":
            case "ver": {
                player.sendMessage("§aYou are using version" + plugin.getDescription().getVersion());
                break;
            }
            case "help": {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F , 3F);
                player.sendMessage(Utils.colorize("&d[&b&m----- &6&lUlti Vote &b&m-----&d]"));
                player.sendMessage(" ");
                player.sendMessage(Utils.colorize("&a/uv help - Shows you this page"));
                player.sendMessage(Utils.colorize("&a/uv rewards - Opens a Gui for the vote rewards"));
                player.sendMessage(Utils.colorize("&a/uv partyrewards - Opens a Gui for the party rewards"));
                player.sendMessage(Utils.colorize("&a/uv version - Shows you the plugin version"));
                player.sendMessage(Utils.colorize("&d[&b&m----------------------------&d]"));
                player.sendMessage(Utils.colorize("&aCopyright (c) 2020 Raznar Lab"));
                break;
            }
            case "partyrewards":
            case "partyreward": {
            }
            default: {
                this.execute(sender, new String[]{"help"});
                break;
            }
        }
    }

}
