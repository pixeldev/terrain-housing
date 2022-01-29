package net.cosmogrp.thousing.utils;

import net.cosmogrp.thousing.TerrainHousingPlugin;
import org.bukkit.ChatColor;

import java.util.List;

public class FileUtils {
    /**
     * Get plugin's prefix
     *
     * @return Plugin prefix
     */
    public static String getPrefix() {
        return ChatColor.translateAlternateColorCodes(
                '&',
                TerrainHousingPlugin.instance()
                        .getConfig()
                        .getString("TerrainHousingPlugin.Prefix")
        );
    }


    /**
     * Get whether TerrainHousingPlugin can override protection plugins or not
     *
     * @return true/false
     */
    public static boolean getOverrideProtections() {
        return TerrainHousingPlugin.instance()
                .getConfig()
                .getBoolean("TerrainHousingPlugin.OverrideProtections");
    }

    /**
     * Set TerrainHousingPlugin override protection plugins
     *
     * @param override Override protections or not
     */
    public static void setOverrideProtections(boolean override) {
        TerrainHousingPlugin.instance()
                .getConfig()
                .set("TerrainHousingPlugin.OverrideProtections", override);
        TerrainHousingPlugin.instance()
                .saveConfig();
    }


    /**
     * Get sign text of Housing available
     *
     * @return String list of sign text
     */
    public static List<String> getSignAvailable() {
        return TerrainHousingPlugin.instance()
                .getConfig()
                .getStringList("TerrainHousingPlugin.SignText.Available");
    }

    /**
     * Get sign text of Housing occupied
     *
     * @return String list of sign text
     */
    public static List<String> getSignOccupied() {
        return TerrainHousingPlugin.instance()
                .getConfig()
                .getStringList("TerrainHousingPlugin.SignText.Occupied");
    }

    /**
     * Get sign text of Housing resetting
     *
     * @return String list of sign text
     */
    public static List<String> getSignResetting() {
        return TerrainHousingPlugin.instance()
                .getConfig()
                .getStringList("TerrainHousingPlugin.SignText.Resetting");
    }
}
