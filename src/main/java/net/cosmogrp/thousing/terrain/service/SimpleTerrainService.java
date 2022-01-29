package net.cosmogrp.thousing.terrain.service;

import net.cosmogrp.thousing.message.MessageHandler;
import net.cosmogrp.thousing.terrain.Terrain;
import net.cosmogrp.thousing.terrain.repo.TerrainRepository;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

public class SimpleTerrainService implements TerrainService {

    @Inject private MessageHandler messageHandler;
    @Inject private TerrainRepository terrainRepository;

    @Override
    public void createTerrain(Player player, String id) {
        Terrain terrain = Terrain.from(id, player);

        terrainRepository.addTerrain(terrain);
        messageHandler.sendMessage(
                player, "terrain.created",
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

        terrain.setSignLocation(targetBlock);
        messageHandler.sendMessage(
                sender, "terrain.set-skull-location",
                "%id%", terrain.getId()
        );
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
                    sender, "terrain.not-sign"
            );
            return;
        }

        terrain.setSkullLocation(targetBlock);
        terrainRepository.updateTerrain(terrain);
        messageHandler.sendMessage(
                sender, "terrain.set-sign-location",
                "%id%", terrain.getId()
        );
    }
}
