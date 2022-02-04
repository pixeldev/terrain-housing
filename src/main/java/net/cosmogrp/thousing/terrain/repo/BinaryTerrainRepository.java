package net.cosmogrp.thousing.terrain.repo;

import net.cosmogrp.thousing.axis.BlockAxis;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BinaryTerrainRepository implements TerrainRepository {

    private final File terrainsFile;

    private final Map<BlockAxis, String> terrainsBySign;
    private final Map<String, Terrain> terrainsById;

    public @Inject BinaryTerrainRepository(Plugin plugin) {
        this.terrainsFile = new File(plugin.getDataFolder(), "terrains.dat");
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
    public Collection<Terrain> getTerrains() {
        return terrainsById.values();
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
    public void loadTerrains() throws Exception {
        if (!makeFile(false)) {
            return;
        }

        try (DataInputStream input = new DataInputStream(
                new FileInputStream(terrainsFile)
        )) {
            int size = input.readInt();
            for (int i = 0; i < size; i++) {
                Terrain terrain = Terrain.from(input);
                addTerrain(terrain);
                terrainsBySign.put(terrain.getSignLocation(), terrain.getId());
            }
        }
    }

    @Override
    public void saveTerrains() throws Exception {
        if (!makeFile(true)) {
            return;
        }

        try (DataOutputStream output = new DataOutputStream(
                new FileOutputStream(terrainsFile)
        )) {
            output.writeInt(terrainsBySign.size());
            for (Terrain terrain : terrainsById.values()) {
                terrain.write(output);
            }
        }
    }

    private boolean makeFile(boolean create) throws IOException {
        if (!terrainsFile.exists() && create) {
            return terrainsFile.createNewFile();
        }

        return terrainsFile.exists();
    }

}
