package net.cosmogrp.thousing.message;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MessageHandler {

    @Inject private FileConfiguration configuration;

    public void sendMessage(
            CommandSender sender,
            String path, String... replacements
    ) {
        sender.sendMessage(makeMessage(path, replacements));
    }

    public String makeMessage(String path, String... replacements) {
        String message = configuration.getString(path);

        if (message == null) {
            return path;
        }

        for (int i = 0 ; i < replacements.length ; i += 2) {
            message = message.replace(
                    replacements[i],
                    replacements[i + 1]
            );
        }

        return translateAlternateColorCodes(
                '&', message
        );
    }
}
