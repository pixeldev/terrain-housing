package net.cosmogrp.thousing.module;

import me.yushust.inject.AbstractModule;
import net.cosmogrp.thousing.TerrainHousingPlugin;
import net.cosmogrp.thousing.message.MessageHandler;
import org.bukkit.configuration.file.FileConfiguration;

public class MainModule extends AbstractModule {

    private final TerrainHousingPlugin plugin;

    public MainModule(TerrainHousingPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void configure() {
        bind(FileConfiguration.class).toInstance(plugin.getConfig());
        bind(MessageHandler.class).singleton();
    }

}
