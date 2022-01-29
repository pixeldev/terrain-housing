package net.cosmogrp.thousing.listener;

import net.cosmogrp.thousing.TerrainHousingPlugin;
import net.cosmogrp.thousing.terrain.Housing;
import net.cosmogrp.thousing.utils.FileUtils;
import net.cosmogrp.thousing.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null) return;

        Location loc = block.getLocation();

        for (Housing housing : TerrainHousingPlugin.instance()
                .getTerrainManager()
                .getTerrains()) {

            Location min = housing.getMin();
            Location max = housing.getMax();
            if (min == null || max == null) continue;

            if (Utils.inRegion(loc, min, max)) {

                if (housing.getOccupied() == null) {
                    if (!player.hasPermission("TerrainHousing.Admin")) {
                        event.setCancelled(true);
                        player.sendMessage(FileUtils.getPrefix() + ChatColor.YELLOW + "Please click the sign to claim before you can modify blocks here.");
                    }
                } else {
                    Player occupied = Bukkit.getPlayer(housing.getOccupied());

                    if (!player.equals(occupied)) {
                        event.setCancelled(true);
                        player.sendMessage(FileUtils.getPrefix() + ChatColor.RED + "You cannot modify blocks in this area because " +
                                ChatColor.YELLOW + occupied.getName() + ChatColor.RED + " is occupied.");
                    } else {
                        if (FileUtils.getOverrideProtections())
                            if (event.isCancelled())
                                event.setCancelled(false);
                    }
                }
            }
        }
    }

}
