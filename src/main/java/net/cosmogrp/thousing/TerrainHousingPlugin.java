package net.cosmogrp.thousing;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.brigadier.BrigadierCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.yushust.inject.Injector;
import net.cosmogrp.thousing.command.TerrainCommand;
import net.cosmogrp.thousing.command.internal.TerrainPartFactory;
import net.cosmogrp.thousing.module.MainModule;
import net.cosmogrp.thousing.terrain.Terrain;
import net.cosmogrp.thousing.terrain.repo.TerrainRepository;
import net.cosmogrp.thousing.user.service.UserService;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Set;
import java.util.logging.Level;

public class TerrainHousingPlugin extends JavaPlugin {

    @Inject private TerrainCommand terrainCommand;
    @Inject private Set<Listener> listeners;
    @Inject private TerrainPartFactory terrainPartFactory;
    @Inject private TerrainRepository terrainRepository;
    @Inject private UserService userService;

    @Override
    public void onLoad() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        Injector.create(new MainModule(this)).injectMembers(this);
    }

    @Override
    public void onEnable() {
        try {
            terrainRepository.loadTerrains();
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Failed to load terrains", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }

        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }

        CommandManager commandManager = new BrigadierCommandManager(this);
        PartInjector partInjector = PartInjector.create();

        partInjector.install(new BukkitModule());
        partInjector.install(new DefaultsModule());
        partInjector.bindFactory(Terrain.class, terrainPartFactory);

        AnnotatedCommandTreeBuilder treeBuilder =
                new AnnotatedCommandTreeBuilderImpl(partInjector);

        commandManager.registerCommands(treeBuilder.fromClass(terrainCommand));
    }

    @Override
    public void onDisable() {
        try {
            terrainRepository.saveTerrains();
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Failed to save terrains", e);
        }

        userService.saveAllUsers();
    }

}
