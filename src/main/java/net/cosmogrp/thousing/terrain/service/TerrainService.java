package net.cosmogrp.thousing.terrain.service;

import net.cosmogrp.thousing.terrain.Terrain;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface TerrainService {

    void createTerrain(Player player, String id);

    void deleteTerrain(Player player, Terrain terrain);

    void teleportToTerrain(Player player, Terrain terrain);

    @Nullable Block getTargetBlock(Player player);

    void setSkullLocation(Player sender, Terrain terrain);

    void setSignLocation(Player sender, Terrain terrain);

}
