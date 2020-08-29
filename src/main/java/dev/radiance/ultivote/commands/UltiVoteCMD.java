package dev.radiance.ultivote.commands;

import dev.radiance.ultivote.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UltiVoteCMD implements CommandExecutor {

    private final Main plugin;

    public UltiVoteCMD(Main instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to execute this command!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("ulti.vote")) {
            Messages.versionMsg(player);
            return false;
        }

        if (args.length < 1) {
            Messages.helpMsg(player);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
            case "rl": {
                plugin.reloadConfig();

                Messages.reloadMsg(player);
                break;
            }

            case "version":
            case "ver": {
                Messages.versionMsg(player);
                break;
            }

            default: {
                Messages.helpMsg(player);
                break;
            }

        }

        return false;
    }
}
