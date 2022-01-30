package net.cosmogrp.thousing.module;

import me.yushust.inject.AbstractModule;
import net.cosmogrp.thousing.TerrainHousingPlugin;
import net.cosmogrp.thousing.message.MessageHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class MainModule extends AbstractModule {

    private final TerrainHousingPlugin plugin;

    public MainModule(TerrainHousingPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void configure() {
        bind(Plugin.class).to(TerrainHousingPlugin.class).singleton();
        bind(Logger.class).toInstance(plugin.getLogger());
        bind(FileConfiguration.class).toInstance(plugin.getConfig());
        bind(MessageHandler.class).singleton();

        install(new TerrainModule(), new UserModule());
    }

}
