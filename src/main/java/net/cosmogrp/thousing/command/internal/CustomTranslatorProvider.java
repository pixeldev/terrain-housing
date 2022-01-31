package net.cosmogrp.thousing.command.internal;

import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.translator.TranslationProvider;
import net.cosmogrp.thousing.message.MessageHandler;

import javax.inject.Inject;

public class CustomTranslatorProvider
        implements TranslationProvider {

    @Inject private MessageHandler messageHandler;

    @Override
    public String getTranslation(Namespace namespace, String key) {
        return messageHandler.makeMessage("commands." + key);
    }
}
