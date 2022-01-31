package net.cosmogrp.thousing.terrain.service;

import net.cosmogrp.thousing.terrain.Terrain;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface TerrainService {

    void createTerrain(Player player, String id);

    void claim(Player player, Terrain terrain);

    void removeClaimed(Terrain terrain);

    void createDefaultSchematic(Player player);

    void setupCuboid(Player player, Terrain terrain);

    void moveOrigin(Player player, Terrain terrain);

    void deleteTerrain(Player player, Terrain terrain);

    void teleportToTerrain(Player player, Terrain terrain);

    @Nullable Block getTargetBlock(Player player);

    void setSkullLocation(Player sender, Terrain terrain);

    void setSignLocation(Player sender, Terrain terrain);

}
