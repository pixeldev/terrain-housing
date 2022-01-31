package net.cosmogrp.thousing.terrain.service;

import me.clip.placeholderapi.PlaceholderAPI;
import net.cosmogrp.thousing.cuboid.Cuboid;
import net.cosmogrp.thousing.cuboid.WorldEditSelection;
import net.cosmogrp.thousing.message.MessageHandler;
import net.cosmogrp.thousing.region.RegionHandler;
import net.cosmogrp.thousing.schematic.SchematicHandler;
import net.cosmogrp.thousing.terrain.Terrain;
import net.cosmogrp.thousing.terrain.repo.TerrainRepository;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class SimpleTerrainService implements TerrainService {

    @Inject private MessageHandler messageHandler;
    @Inject private TerrainRepository terrainRepository;
    @Inject private WorldEditSelection worldEditSelection;
    @Inject private SchematicHandler schematicHandler;
    @Inject private RegionHandler regionHandler;

    @Override
    public void createTerrain(Player player, String id) {
        if (terrainRepository.getTerrain(id) != null) {
            messageHandler.sendMessage(
                    player, "terrain.already-exists",
                    "%id%", id
            );
            return;
        }

        Terrain terrain = Terrain.from(id, player);

        terrainRepository.addTerrain(terrain);
        messageHandler.sendMessage(
                player, "terrain.created",
                "%id%", terrain.getId()
        );

        sendMissingProperties(player, terrain);
    }

    @Override
    public void claim(Player player, Terrain terrain) {
        terrain.setClaimedBy(player.getUniqueId());

        Sign sign = terrain.getSignBlock();
        sign.setEditable(true);

        List<String> lines =messageHandler.makeMessages(
                "terrain.claimed-sign"
        );

        for (int i = 0; i < lines.size(); i++) {
            sign.setLine(i, PlaceholderAPI.setPlaceholders(
                    player, lines.get(i)
            ));
        }

        sign.update();
        sign.setEditable(false);

        Skull skull = terrain.getSkullBlock();
        skull.setOwningPlayer(player);
        skull.update();
    }

    @Override
    public void removeClaimed(Terrain terrain) {
        terrain.setClaimedBy(null);

        Sign sign = terrain.getSignBlock();
        sign.setEditable(true);

        List<String> lines =messageHandler.makeMessages(
                "terrain.unclaimed-sign"
        );

        for (int i = 0; i < lines.size(); i++) {
            sign.setLine(i, lines.get(i));
        }

        sign.update();
        sign.setEditable(false);

        Skull skull = terrain.getSkullBlock();

        skull.setOwningPlayer(Bukkit.getOfflinePlayer(
                UUID.fromString("8667ba71-b85a-4004-af54-457a9734eed7")
        ));

        skull.update();
    }

    @Override
    public void createDefaultSchematic(Player player) {
        Cuboid cuboid = worldEditSelection
                .createCuboidFromSelection(player);

        if (cuboid != null) {
            schematicHandler.saveSchematic("default", cuboid);
            messageHandler.sendMessage(player, "terrain.default-schematic-created");
        }
    }

    @Override
    public void setupCuboid(Player player, Terrain terrain) {
        Cuboid cuboid = worldEditSelection
                .createCuboidFromSelection(player);

        if (cuboid != null) {
            terrain.setCuboid(cuboid);
            messageHandler.sendMessage(
                    player, "terrain.set-cuboid",
                    "%id%", terrain.getId()
            );
            regionHandler.removeRegion(terrain);
            regionHandler.createRegion(terrain);
            sendMissingProperties(player, terrain);
        }
    }

    @Override
    public void moveOrigin(Player player, Terrain terrain) {
        terrain.setOriginLocation(player);
        messageHandler.sendMessage(
                player, "terrain.moved-origin",
                "%id%", terrain.getId()
        );
    }

    @Override
    public void deleteTerrain(Player player, Terrain terrain) {
        terrainRepository.removeTerrain(terrain);
        messageHandler.sendMessage(
                player, "terrain.deleted",
                "%id%", terrain.getId()
        );
    }

    @Override
    public void teleportToTerrain(Player player, Terrain terrain) {
        player.teleport(terrain.getOriginLocation().toLocation());
        messageHandler.sendMessage(
                player, "terrain.teleported",
                "%id%", terrain.getId()
        );
    }

    @Override
    public @Nullable Block getTargetBlock(Player player) {
        Block block = player.getTargetBlock(null, 4);

        if (block.getType() == Material.AIR) {
            messageHandler.sendMessage(
                    player, "terrain.no-target-block"
            );
            return null;
        }

        return block;
    }

    @Override
    public void setSkullLocation(
            Player sender, Terrain terrain
    ) {
        Block targetBlock = getTargetBlock(sender);

        if (targetBlock == null) {
            return;
        }

        if (targetBlock.getType() != Material.PLAYER_HEAD) {
            messageHandler.sendMessage(
                    sender, "terrain.no-skull-block"
            );
            return;
        }

        terrain.setSkullLocation(targetBlock);
        messageHandler.sendMessage(
                sender, "terrain.set-skull-location",
                "%id%", terrain.getId()
        );
        sendMissingProperties(sender, terrain);
    }

    @Override
    public void setSignLocation(
            Player sender, Terrain terrain
    ) {
        Block targetBlock = getTargetBlock(sender);

        if (targetBlock == null) {
            return;
        }

        if (!(targetBlock.getState() instanceof Sign)) {
            messageHandler.sendMessage(
                    sender, "terrain.no-sign-block"
            );
            return;
        }

        terrain.setSignLocation(targetBlock);
        terrainRepository.updateTerrain(terrain);
        messageHandler.sendMessage(
                sender, "terrain.set-sign-location",
                "%id%", terrain.getId()
        );

        sendMissingProperties(sender, terrain);
    }

    private void sendMissingProperties(
            Player player,
            Terrain terrain
    ) {
        StringBuilder message = new StringBuilder();
        boolean completed = true;

        if (terrain.getCuboid() == null) {
            message.append(messageHandler.makeMessage("terrain.missing-cuboid"))
                    .append("\n");
            completed = false;
        }

        if (terrain.getSignLocation() == null) {
            message.append(messageHandler.makeMessage("terrain.missing-sign"))
                    .append("\n");
            completed = false;
        }

        if (terrain.getSkullLocation() == null) {
            message.append(messageHandler.makeMessage("terrain.missing-skull"))
                    .append("\n");
            completed = false;
        }

        if (completed) {
            terrain.setEnabled(true);
            messageHandler.sendMessage(
                    player, "terrain.setup-completed",
                    "%id%", terrain.getId()
            );
        } else {
            messageHandler.sendMessages(
                    player, "terrain.missing-setup",
                    "%id%", terrain.getId(),
                    "%missing%", message.toString()
            );
        }
    }
}
