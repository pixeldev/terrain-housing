package net.cosmogrp.thousing.terrain.repo;

import net.cosmogrp.thousing.block.BlockAxis;
import net.cosmogrp.thousing.terrain.Terrain;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BinaryTerrainRepository implements TerrainRepository {

    private final File terrainsFile;

    private final Map<BlockAxis, String> terrainsBySign;
    private final Map<String, Terrain> terrainsById;

    public @Inject BinaryTerrainRepository(Plugin plugin)
            throws IOException {
        this.terrainsFile = new File(plugin.getDataFolder(), "terrains.dat");

        if (!terrainsFile.exists()) {
            if (!terrainsFile.createNewFile()) {
                throw new IOException("Could not create terrains file");
            }
        }

        this.terrainsBySign = new HashMap<>();
        this.terrainsById = new HashMap<>();
    }

    @Override
    public @Nullable Terrain getTerrain(Block signBlock) {
        String terrainId = terrainsBySign.get(BlockAxis.from(
                signBlock.getLocation()
        ));

        if (terrainId == null) {
            return null;
        }

        return getTerrain(terrainId);
    }

    @Override
    public @Nullable Terrain getTerrain(String id) {
        return terrainsById.get(id);
    }

    @Override
    public void updateTerrain(Terrain terrain) {
        terrainsBySign.put(terrain.getSignLocation(), terrain.getId());
    }

    @Override
    public void addTerrain(Terrain terrain) {
        terrainsById.put(terrain.getId(), terrain);
    }

    @Override
    public void removeTerrain(Terrain terrain) {
        terrainsBySign.remove(terrain.getSignLocation());
        terrainsById.remove(terrain.getId());
    }

    @Override
    public void loadTerrains() throws Exception {
        try (DataInputStream input = new DataInputStream(
                new FileInputStream(terrainsFile)
        )) {
            int size = input.readInt();
            for (int i = 0; i < size; i++) {
                addTerrain(Terrain.from(input));
            }
        }
    }

    @Override
    public void saveTerrains() throws Exception {
        try (DataOutputStream output = new DataOutputStream(
                new FileOutputStream(terrainsFile)
        )) {
            output.writeInt(terrainsBySign.size());
            for (Terrain terrain : terrainsById.values()) {
                terrain.write(output);
            }
        }
    }

}
