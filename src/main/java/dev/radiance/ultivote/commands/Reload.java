package dev.radiance.ultivote.commands;

import dev.radiance.ultivote.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Config.reload();
        player.sendMessage("§aConfig has been reloaded!");
        Bukkit.getLogger().info(player.getName() + "Has reloaded the config");
        return true;
    }
}
