package net.cosmogrp.thousing.schematic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import net.cosmogrp.thousing.cuboid.Cuboid;
import net.cosmogrp.thousing.terrain.ClaimedTerrain;
import net.cosmogrp.thousing.terrain.Terrain;
import net.cosmogrp.thousing.terrain.repo.TerrainRepository;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class WorldEditSchematicHandler implements SchematicHandler {

    @Inject private TerrainRepository repository;
    @Inject private Logger pluginLogger;

    private final File schematicFolder;

    public @Inject WorldEditSchematicHandler(FileConfiguration configuration) {
        String schematicPath = configuration.getString("schematic.path");

        if (schematicPath == null) {
            throw new IllegalArgumentException("schematic.path is not set in config.yml");
        }

        this.schematicFolder = new File(schematicPath);

        if (!this.schematicFolder.exists()) {
            if (!this.schematicFolder.mkdirs()) {
                throw new IllegalArgumentException("Could not create schematic folder");
            }
        }
    }

    @Override
    public void pasteSchematic(ClaimedTerrain terrain) {

    }

    @Override
    public void saveSchematic(ClaimedTerrain terrain) {
        Terrain realTerrain =
                repository.getTerrain(terrain.getTerrainId());

        if (realTerrain == null) {
            pluginLogger.severe("Could not find terrain with id "
                    + terrain.getTerrainId());
            return;
        }

        Cuboid cuboid = realTerrain.getCuboid();

        if (cuboid == null) {
            return;
        }

        File file = createSchematicFile(terrain);
        Region region = cuboid.toWorldEditCuboid();
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        try (EditSession editSession = WorldEdit.getInstance()
                .newEditSession(region.getWorld())
        ) {
            ForwardExtentCopy copy = new ForwardExtentCopy(
                    editSession, region,
                    clipboard, region.getMinimumPoint()
            );

            copy.setCopyingEntities(true);

            try {
                Operations.complete(copy);
            } catch (WorldEditException e) {
                pluginLogger.severe("Could not copy region " +
                        terrain.getTerrainId() +
                        " to clipboard");
            }

            try (ClipboardWriter writer = BuiltInClipboardFormat
                    .SPONGE_SCHEMATIC
                    .getWriter(new FileOutputStream(file))
            ) {
                writer.write(clipboard);
            } catch (IOException e) {
                pluginLogger.severe("Could not write schematic file " +
                        file.getName());
            }
        }
    }

    private Clipboard createClipboard(File file) {
        ClipboardFormat format = ClipboardFormats.findByFile(file);

        if (format == null) {
            // I think this should never happen
            pluginLogger.severe("Could not find schematic format for "
                    + file.getName());
            return null;
        }

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            return reader.read();
        } catch (IOException e) {
            pluginLogger.severe("Could not read schematic file "
                    + file.getName());
            return null;
        }
    }

    private File createSchematicFile(ClaimedTerrain terrain) {
        File file = new File(
                schematicFolder,
                terrain.getOwnerId() + ".schematic"
        );

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    pluginLogger.severe(
                            "Could not create schematic file for " +
                                    terrain.getOwnerId()
                    );
                }
            } catch (IOException e) {
                pluginLogger.severe(
                        "Could not create schematic file for "
                                + terrain.getOwnerId()
                );
            }
        }

        return file;
    }
}