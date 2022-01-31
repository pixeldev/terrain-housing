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
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import net.cosmogrp.thousing.cuboid.Cuboid;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class WorldEditSchematicHandler implements SchematicHandler {

    @Inject private Logger pluginLogger;
    private final File schematicFolder;

    public @Inject WorldEditSchematicHandler(FileConfiguration configuration) {
        this.schematicFolder = new File(
                configuration.getString("storage-path"),
                "schematics"
        );

        if (!this.schematicFolder.exists()) {
            if (!this.schematicFolder.mkdirs()) {
                throw new IllegalArgumentException("Could not create schematic folder");
            }
        }
    }

    @Override
    public void pasteSchematic(String schematic, Cuboid cuboid) {
        Region region = cuboid.toWorldEditCuboid();
        File file = createSchematicFile(schematic, false);

        if (file == null) {
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);

        if (format == null) {
            // this should never happen
            return;
        }

        try (EditSession editSession = WorldEdit.getInstance()
                .newEditSession(region.getWorld())
        ) {
            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                Clipboard clipboard = reader.read();
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(region.getMinimumPoint())
                        .ignoreAirBlocks(false)
                        .copyEntities(true)
                        .build();

                Operations.complete(operation);
            } catch (IOException | WorldEditException e) {
                pluginLogger.severe(
                        "Couldn't paste schematic file " +
                                file.getName()
                );
            }
        }
    }

    @Override
    public void saveSchematic(String schematic, Cuboid cuboid) {
        Region region = cuboid.toWorldEditCuboid();
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        File file = createSchematicFile(schematic, true);

        if (file == null) {
            return;
        }

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
                pluginLogger.severe(
                        "Couldn't copy region in file "
                                + file.getName());
            }

            try (ClipboardWriter writer = BuiltInClipboardFormat
                    .SPONGE_SCHEMATIC
                    .getWriter(new FileOutputStream(file))
            ) {
                writer.write(clipboard);
            } catch (IOException e) {
                pluginLogger.severe(
                        "Could not write schematic file " +
                        file.getName());
            }
        }
    }

    private @Nullable File createSchematicFile(
            String schematic,
            boolean create
    ) {
        File file = new File(
                schematicFolder,
                schematic + ".schematic"
        );

        boolean created = file.exists();

        if (create && !created) {
            try {
                created = file.createNewFile();
                if (!created) {
                    pluginLogger.severe(
                            "Couldn't create schematic file for "
                                    + schematic);
                }
            } catch (IOException e) {
                pluginLogger.severe(
                        "Could not create schematic file for "
                                + schematic
                );
            }
        }

        return created ? file : null;
    }
}
