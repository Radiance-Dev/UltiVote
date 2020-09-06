package com.raznar.ultivote.utils;

import org.bukkit.ChatColor;

public class Utils {

    /**
     * Translates the raw text into a Minecraft colored text
     */
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Determines if a string is a valid minecraft username
     */
    public static boolean isMinecraftName(String name) {
        return name.matches("[a-zA-Z0-9-_]{3,16}");
    }

}
