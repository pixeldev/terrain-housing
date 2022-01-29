package net.cosmogrp.thousing.listener;

import net.cosmogrp.thousing.TerrainHousingPlugin;
import net.cosmogrp.thousing.terrain.Housing;
import net.cosmogrp.thousing.utils.FileUtils;
import net.cosmogrp.thousing.utils.SchematicUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerConnectionListener implements Listener {
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        for (Housing housing : TerrainHousingPlugin.instance()
                .getTerrainManager()
                .getTerrains()) {
            if (housing.getOccupied() == null) continue;

            if (housing.getOccupied().equals(uuid)) {

                // Stop countdown
                housing.stopCd();

                // Lock sign
                housing.setSignLock(uuid);

                // Save schematic
                SchematicUtils.save(housing.getId(), uuid.toString());

                // Paste schematic
                SchematicUtils.paste(housing.getId(), "default");

                // Remove occupation
                housing.setOccupied(null);

                // Update sign
                Sign signBlock = (Sign) housing.getSign().getBlock().getState();
                for (int i = 0; i < 4; i++) {
                    String line = ChatColor.translateAlternateColorCodes('&', FileUtils.getSignResetting().get(i));
                    signBlock.setLine(i, line);
                }
                signBlock.update();

                // Delay 3 seconds
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Remove sign lock
                        housing.removeSignLock();

                        // Update sign
                        for (int i = 0; i < 4; i++) {
                            String line = ChatColor.translateAlternateColorCodes('&', FileUtils.getSignAvailable().get(i));
                            signBlock.setLine(i, line);
                        }
                        signBlock.update();

                        // Update skull
                        if (housing.getSkull() != null)
                            housing.updateSkull("8667ba71-b85a-4004-af54-457a9734eed7");
                    }
                };
                runnable.runTaskLater(TerrainHousingPlugin.instance(), 3 * 20);
            }
        }
    }
    
}
