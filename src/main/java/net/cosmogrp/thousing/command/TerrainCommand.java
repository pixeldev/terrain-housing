package net.cosmogrp.thousing.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.cosmogrp.thousing.terrain.Terrain;
import net.cosmogrp.thousing.terrain.service.TerrainService;
import net.cosmogrp.thousing.user.service.UserService;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;

@Command(names = "terrain")
public class TerrainCommand implements CommandClass {

    @Inject private TerrainService terrainService;
    @Inject private Plugin plugin;
    @Inject private UserService userService;

    @Command(names = "create", permission = "terrain.create")
    public void runCreate(@Sender Player sender, String id) {
        terrainService.createTerrain(sender, id);
    }

    @Command(names = "reload", permission = "terrain.reload")
    public void runReload(@Sender Player sender) {
        plugin.reloadConfig();
        sender.sendMessage("Config reloaded");
    }

    @Command(names = "add")
    public void runAuthorize(@Sender Player sender, OfflinePlayer target) {
        userService.authorizePlayer(sender, target);
    }

    @Command(names = "remove")
    public void runDisavow(@Sender Player sender, OfflinePlayer target) {
        userService.disavowPlayer(sender, target);
    }

    @Command(names = "toggle", permission = "terrain.toggle")
    public void runDisable(@Sender Player sender, Terrain terrain) {
        terrainService.toggleTerrain(sender, terrain);
    }

    @Command(names = "default-schematic", permission = "terrain.default-schematic")
    public void runSetDefaultSchematic(@Sender Player sender) {
        terrainService.createDefaultSchematic(sender);
    }

    @Command(names = "cuboid", permission = "terrain.cuboid")
    public void runCuboid(@Sender Player sender, Terrain terrain) {
        terrainService.setupCuboid(sender, terrain);
    }

    @Command(names = "origin", permission = "terrain.origin")
    public void runSetOrigin(@Sender Player sender, Terrain terrain) {
        terrainService.moveOrigin(sender, terrain);
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
        terrainService.toggleTerrain(sender, terrain);
    }

}
