package net.cosmogrp.thousing.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.cosmogrp.thousing.terrain.Terrain;
import net.cosmogrp.thousing.terrain.service.TerrainService;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(names = "terrain")
public class TerrainCommand implements CommandClass {

    @Inject private TerrainService terrainService;

    @Command(names = "create", permission = "terrain.create")
    public void runCreate(@Sender Player sender, String id) {
        terrainService.createTerrain(sender, id, sender.getLocation().getBlock());
    }

    @Command(names = "teleport", permission = "terrain.teleport")
    public void runTeleport(@Sender Player sender, Terrain terrain) {
        terrainService.teleportToTerrain(sender, terrain);
    }

    @Command(names = "sign", permission = "terrain.sign")
    public void runSetSign(@Sender Player sender, Terrain terrain) {
        terrainService.setSignLocation(sender, terrain);
    }

    @Command(names = "skull", permission = "terrain.skull")
    public void runSetSkull(@Sender Player sender, Terrain terrain) {
        terrainService.setSkullLocation(sender, terrain);
    }

    @Command(names = "delete", permission = "terrain.delete")
    public void runDelete(@Sender Player sender, Terrain terrain) {
        terrainService.deleteTerrain(sender, terrain);
    }

}