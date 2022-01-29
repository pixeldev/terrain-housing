package net.cosmogrp.thousing.terrain.service;

import net.cosmogrp.thousing.message.MessageHandler;
import net.cosmogrp.thousing.terrain.Terrain;
import net.cosmogrp.thousing.terrain.repo.TerrainRepository;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SimpleTerrainService implements TerrainService {

    @Inject private MessageHandler messageHandler;
    @Inject private TerrainRepository terrainRepository;

    @Override
    public void createTerrain(Player player, String id, Block originBlock) {
        Terrain terrain = Terrain.from(id, originBlock);

        terrainRepository.addTerrain(terrain);
        messageHandler.sendMessage(
                player, "terrain.created",
                "%id%", terrain.getId()
        );
    }

    @Override
    public void setSkullLocation(
            Player sender, Terrain terrain,
            Block block
    ) {
        terrain.setSignLocation(block);
        messageHandler.sendMessage(
                sender, "terrain.set-skull-location",
                "%id%", terrain.getId()
        );
    }

    @Override
    public void setSignLocation(
            Player sender, Terrain terrain,
            Block block
    ) {
        terrain.setSkullLocation(block);
        terrainRepository.updateTerrain(terrain);
        messageHandler.sendMessage(
                sender, "terrain.set-sign-location",
                "%id%", terrain.getId()
        );
    }
}
