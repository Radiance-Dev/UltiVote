package dev.radiance.ultivote.commands;

import dev.radiance.ultivote.Main;
import dev.radiance.ultivote.utils.BetterCommand;
import dev.radiance.ultivote.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.List;

@RequiredArgsConstructor
public class VoteCMD extends BetterCommand {

    private final Main plugin;

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> messages = plugin.getConfig().getStringList("messages.vote-list");
        String fullMessages = Utils.colorize(String.join("\n", messages));

        sender.sendMessage(fullMessages);
    }

}
