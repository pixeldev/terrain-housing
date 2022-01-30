package net.cosmogrp.thousing.module;

import me.yushust.inject.AbstractModule;
import net.cosmogrp.thousing.TerrainHousingPlugin;
import net.cosmogrp.thousing.listener.PlayerConnectionListener;
import net.cosmogrp.thousing.listener.PlayerInteractListener;
import net.cosmogrp.thousing.message.MessageHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MainModule extends AbstractModule {

    private final TerrainHousingPlugin plugin;

    public MainModule(TerrainHousingPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void configure() {
        bind(Executor.class).toInstance(Executors.newFixedThreadPool(3));
        bind(Plugin.class).to(TerrainHousingPlugin.class).singleton();
        bind(Logger.class).toInstance(plugin.getLogger());
        bind(FileConfiguration.class).toInstance(plugin.getConfig());
        bind(MessageHandler.class).singleton();

        multibind(Listener.class)
                .asSet()
                .to(PlayerInteractListener.class)
                .to(PlayerConnectionListener.class);

        install(new TerrainModule(), new UserModule());
    }

}
