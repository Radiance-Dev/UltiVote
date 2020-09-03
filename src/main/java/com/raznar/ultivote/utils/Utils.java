package com.raznar.ultivote.utils;

import org.bukkit.ChatColor;

public class Utils {

    /**
     * Translates the raw text into a Minecraft colored text
     */
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
