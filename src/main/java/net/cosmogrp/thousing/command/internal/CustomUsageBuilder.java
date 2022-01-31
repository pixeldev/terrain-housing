package net.cosmogrp.thousing.command.internal;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.usage.DefaultUsageBuilder;
import net.cosmogrp.thousing.message.MessageHandler;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import javax.inject.Inject;

public class CustomUsageBuilder extends DefaultUsageBuilder {

    @Inject private MessageHandler messageHandler;

    @Override
    public Component getUsage(CommandContext commandContext) {
        Component usage = super.getUsage(commandContext);
        return TextComponent.of(messageHandler.makeMessage("commands.usage"))
                .append(usage.color(TextColor.RED));
    }
}
