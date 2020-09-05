package com.raznar.ultivote.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class BetterCommand implements CommandExecutor {

    /**
     * Called when command is executed
     *
     * @param sender the command sender
     * @param args the arguments
     */
    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.execute(sender, args);
        return true;
    }

}
