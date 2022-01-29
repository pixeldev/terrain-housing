package net.cosmogrp.thousing.terrain.service;

import net.cosmogrp.thousing.terrain.Terrain;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface TerrainService {

    void createTerrain(Player player, String id, Block originBlock);

    void setSkullLocation(
            Player sender, Terrain terrain,
            Block block
    );

    void setSignLocation(
            Player sender, Terrain terrain,
            Block block
    );

}
