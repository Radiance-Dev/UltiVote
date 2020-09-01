package dev.radiance.ultivote.commands;

import dev.radiance.ultivote.Main;
import dev.radiance.ultivote.utils.BetterCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class UltiVoteCMD extends BetterCommand {

    private final Main plugin;

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

            case "version":
            case "ver": {
                player.sendMessage("§aYou are using version" + plugin.getDescription().getVersion());
                break;
            }
            case "help": {
                // todo: sends the help message
                break;
            }
            default: {
                this.execute(sender, new String[]{"help"});
                break;
            }
        }
    }

}
