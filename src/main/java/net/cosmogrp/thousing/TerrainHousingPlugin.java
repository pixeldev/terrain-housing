package net.cosmogrp.thousing;

import me.yushust.inject.Injector;
import net.cosmogrp.thousing.module.MainModule;
import org.bukkit.plugin.java.JavaPlugin;

public class TerrainHousingPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        Injector.create(new MainModule(this)).injectMembers(this);
    }

    @Override
    public void onEnable() {

    }

}
