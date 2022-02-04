package net.cosmogrp.thousing.terrain.repo;

import net.cosmogrp.thousing.terrain.Terrain;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface TerrainRepository {

    @Nullable Terrain getTerrain(Block signBlock);

    @Nullable Terrain getTerrain(String id);

    Collection<Terrain> getTerrains();

    void updateTerrain(Terrain terrain);

    void addTerrain(Terrain terrain);

    void loadTerrains() throws Exception;

    void saveTerrains() throws Exception;

}
