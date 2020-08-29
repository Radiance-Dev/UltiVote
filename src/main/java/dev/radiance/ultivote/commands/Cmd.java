package dev.radiance.ultivote.commands;

import dev.radiance.ultivote.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Cmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (sender != null) {
            if (!player.hasPermission("ulti.vote")) {
                Messages.versionMsg(player);
            } else {
                if (args.length < 1) {
                    Messages.helpMsg(player);
                } else {
                    if(args[0].isEmpty()) {
                        Messages.helpMsg(player);
                    } else if (args[0].equalsIgnoreCase("help")) {
                        Messages.helpMsg(player);
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        Config.reload();
                        Messages.reloadMsg(player);
                    } else if (args[0].equalsIgnoreCase("version")) {
                        Messages.versionMsg(player);
                    }
                }
            }
        } else {
            Bukkit.getLogger().info("You have to be Player to execute this command!");
        }
        return false;
    }
}
