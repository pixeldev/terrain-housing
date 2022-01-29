package net.cosmogrp.thousing.terrain;

import net.cosmogrp.thousing.TerrainHousingPlugin;
import net.cosmogrp.thousing.utils.FileUtils;
import net.cosmogrp.thousing.utils.SchematicUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TerrainManager {

    private TerrainHousingPlugin plugin;

    private Map<String, Housing> housingList;

    public TerrainManager(TerrainHousingPlugin plugin) {
        this.plugin = plugin;

        this.housingList = new ConcurrentHashMap<>();
    }


    public void unclaimAll() {
        for (Housing housing : housingList.values()) {
            unclaim(housing);
        }
    }

    public void unclaim(Housing housing) {
        // Stop countdown
        housing.stopCd();

        housing.removeSignLock();

        // Save schematic
        if (housing.getOccupied() != null)
            SchematicUtils.save(housing.getId(), housing.getOccupied().toString());

        // Paste schematic
        SchematicUtils.paste(housing.getId(), "default");

        // Remove occupation
        housing.setOccupied(null);

        // Update sign
        try {
            Sign signBlock = (Sign) housing.getSign().getBlock().getState();
            for (int i = 0; i < 4; i++) {
                String line = ChatColor.translateAlternateColorCodes('&', FileUtils.getSignAvailable().get(i));
                signBlock.setLine(i, line);
            }
            signBlock.update();
        } catch (Exception e) {}

        // Update skull
        housing.updateSkull("8667ba71-b85a-4004-af54-457a9734eed7");

        plugin.saveConfig();
    }


    public void load() {
        housingList.clear();

        FileConfiguration conf = plugin.getConfig();
        for (String id : conf.getConfigurationSection("TerrainHousingPlugin.Location").getKeys(false)) {
            try {
                long idle = conf.getLong("TerrainHousingPlugin.Location." + id + ".Idle");
                Location min = (Location) conf.get("TerrainHousingPlugin.Location." + id + ".Minimum");
                Location max = (Location) conf.get("TerrainHousingPlugin.Location." + id + ".Maximum");
                Location origin = (Location) conf.get("TerrainHousingPlugin.Location." + id + ".Origin");
                Location sign = (Location) conf.get("TerrainHousingPlugin.Location." + id + ".Sign");
                Location skull = (Location) conf.get("TerrainHousingPlugin.Location." + id + ".Skull");

                Housing housing = new Housing(id, idle, min, max, origin, sign, skull);
                housingList.put(id, housing);

            } catch (Exception e) {
                plugin.getLogger().warning("Error loading Housing " + id + ".");
                e.printStackTrace();
            }
        }
    }

    public void save() {
        FileConfiguration conf = plugin.getConfig();

        for (Housing housing : housingList.values()) {
            conf.set("TerrainHousingPlugin.Location." + housing.getId() + ".Idle", housing.getIdle());
            conf.set("TerrainHousingPlugin.Location." + housing.getId() + ".Minimum", housing.getMin());
            conf.set("TerrainHousingPlugin.Location." + housing.getId() + ".Maximum", housing.getMax());
            conf.set("TerrainHousingPlugin.Location." + housing.getId() + ".Origin", housing.getOrigin());
            conf.set("TerrainHousingPlugin.Location." + housing.getId() + ".Sign", housing.getSign());
            conf.set("TerrainHousingPlugin.Location." + housing.getId() + ".Skull", housing.getSkull());
        }
        plugin.saveConfig();
    }

    public void delete(String id) {
        plugin.getConfig().set("TerrainHousingPlugin.Location." + id, null);
        plugin.saveConfig();
    }


    public Collection<Housing> getTerrains() {
        return housingList.values();
    }

    public void addTerrain(Housing terrain) {
        housingList.put(terrain.getId(), terrain);
    }

    public void removeTerrain(Housing terrain) {
        removeTerrain(terrain.getId());
    }

    public void removeTerrain(String id) {
        housingList.remove(id);
    }


    public Housing getHousingByName(String id) {
        return housingList.get(id);
    }

}
