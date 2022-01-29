package net.cosmogrp.thousing;

import net.cosmogrp.thousing.command.MainCommand;
import net.cosmogrp.thousing.listener.BlockPlaceListener;
import net.cosmogrp.thousing.listener.BreakBlockListener;
import net.cosmogrp.thousing.listener.PlayerConnectionListener;
import net.cosmogrp.thousing.listener.PlayerInteractListener;
import net.cosmogrp.thousing.listener.SignClickListener;
import net.cosmogrp.thousing.terrain.TerrainManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TerrainHousingPlugin extends JavaPlugin {

    private TerrainManager terrainManager;
    public static File configFile;

    public void onEnable() {
        terrainManager = new TerrainManager(this);
        // Load everything from config
        // -> Load everything from TerrainManager
        // -> In TerrainManager, loop all things in the config
        //    and add to HashMap
        terrainManager.load();

        File file = new File(getDataFolder(), "schematics");
        if (!file.isDirectory())
            file.mkdirs();


        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Configuration not found. Generating the default one.");

            getConfig().options().copyDefaults(true);
            saveConfig();
        }


        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BreakBlockListener(), this);
        pm.registerEvents(new BlockPlaceListener(), this);
        pm.registerEvents(new PlayerConnectionListener(), this);
        pm.registerEvents(new PlayerInteractListener(), this);
        pm.registerEvents(new SignClickListener(), this);

        getCommand("terrainhousing").setExecutor(new MainCommand());
    }

    public void onDisable() {
        // Loop through HashMap
        // -> Save their data into config
        terrainManager.save();
        // Unclaim all Housing and reset to default
        terrainManager.unclaimAll();
    }

    public TerrainManager getTerrainManager() {
        return this.terrainManager;
    }

    public static TerrainHousingPlugin instance() {
        return JavaPlugin.getPlugin(TerrainHousingPlugin.class);
    }

}
