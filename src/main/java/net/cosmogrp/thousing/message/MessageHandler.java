package net.cosmogrp.thousing.message;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;

import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MessageHandler {

    @Inject private FileConfiguration configuration;

    public void sendMessage(
            CommandSender sender,
            String path, String... replacements
    ) {
        sender.sendMessage(makeMessage(path, replacements));
    }

    public void sendMessages(
            CommandSender sender,
            String path, String... replacements
    ) {
        sender.sendMessage(String.join(
                "\n",
                makeMessages(path, replacements)
        ));
    }

    public List<String> makeMessages(String path, String... replacements) {
        List<String> messages = configuration.getStringList("messages." + path);
        messages.replaceAll(line -> applyReplacements(line, replacements));
        return messages;
    }

    public String makeMessage(String path, String... replacements) {
        String message = configuration.getString("messages." + path);

        if (message == null) {
            return path;
        }

        return applyReplacements(message, replacements);
    }

    private String applyReplacements(String message, String... replacements) {
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
