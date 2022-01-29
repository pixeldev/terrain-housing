package net.cosmogrp.thousing.terrain.repo;

import net.cosmogrp.thousing.terrain.Terrain;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

public interface TerrainRepository {

    @Nullable Terrain getTerrain(Block signBlock);

    @Nullable Terrain getTerrain(String id);

    void updateTerrain(Terrain terrain);

    void addTerrain(Terrain terrain);

    void removeTerrain(Terrain terrain);

    void loadTerrains() throws Exception;

    void saveTerrains() throws Exception;

}
