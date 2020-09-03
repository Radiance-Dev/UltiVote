package com.raznar.ultivote.commands;

import com.raznar.ultivote.utils.BetterCommand;
import com.raznar.ultivote.utils.Utils;
import com.raznar.ultivote.Main;
import org.bukkit.command.CommandSender;

import java.util.List;

public class VoteCMD extends BetterCommand {

    private final Main plugin;

    public VoteCMD(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> messages = plugin.getConfig().getStringList("messages.vote-list");
        String fullMessages = Utils.colorize(String.join("\n", messages));

        sender.sendMessage(fullMessages);
    }

}
