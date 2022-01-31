package net.cosmogrp.thousing.user.service;

import net.cosmogrp.thousing.cuboid.Cuboid;
import net.cosmogrp.thousing.message.MessageHandler;
import net.cosmogrp.thousing.region.RegionHandler;
import net.cosmogrp.thousing.schematic.SchematicHandler;
import net.cosmogrp.thousing.terrain.ClaimedTerrain;
import net.cosmogrp.thousing.terrain.Terrain;
import net.cosmogrp.thousing.terrain.repo.TerrainRepository;
import net.cosmogrp.thousing.terrain.service.TerrainService;
import net.cosmogrp.thousing.user.User;
import net.cosmogrp.thousing.user.repo.UserRepository;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.UUID;

public class SimpleUserService implements UserService {

    @Inject private TerrainRepository terrainRepository;
    @Inject private TerrainService terrainService;
    @Inject private SchematicHandler schematicHandler;
    @Inject private UserRepository userRepository;
    @Inject private RegionHandler regionHandler;
    @Inject private MessageHandler messageHandler;

    @Override
    public void tryToClaim(Player player, Block signBlock) {
        Terrain terrain = terrainRepository.getTerrain(signBlock);

        if (terrain == null) {
            return;
        }

        tryToClaim(player, terrain);
    }

    @Override
    public void tryToClaim(Player player, Terrain terrain) {
        if (!player.hasPermission("thousing.claim")) {
            messageHandler.sendMessages(player, "user.no-claim-permission");
            return;
        }

        if (!terrain.isEnabled()) {
            messageHandler.sendMessage(player, "terrain.disabled");
            return;
        }

        UUID claimedBy = terrain.getClaimedBy();

        if (claimedBy != null) {
            if (claimedBy.equals(player.getUniqueId())) {
                messageHandler.sendMessage(
                        player, "terrain.already-claimed-by-you"
                );
            } else {
                messageHandler.sendMessage(
                        player,
                        "terrain.already-claimed-by-other"
                );
            }
            return;
        }

        User user = userRepository.getOrCreate(player);
        ClaimedTerrain claimedTerrain = user.getClaimedTerrain();

        if (claimedTerrain != null) {
            if (claimedTerrain.isLoaded()) {
                messageHandler.sendMessage(
                        player, "user.already-claimed-terrain"
                );
                return;
            }

            claimedTerrain.setTerrainId(terrain.getId());
            schematicHandler.pasteSchematic(
                    user.getPlayerId().toString(),
                    terrain.getCuboid()
            );
        } else {
            claimedTerrain = ClaimedTerrain.from(terrain.getId(), player);
            user.setClaimedTerrain(claimedTerrain);
        }

        regionHandler.authorizePlayers(terrain, claimedTerrain);
        terrainService.claim(player, terrain);
        messageHandler.sendMessage(player, "terrain.claimed");
    }

    @Override
    public void loadUser(Player player) {
        userRepository.loadUser(player);
    }

    @Override
    public void saveUser(Player player) {
        User user = userRepository.saveUser(player);

        if (user != null) {
            saveSyncUser(user);
        }
    }

    private void saveSyncUser(User user) {
        ClaimedTerrain claimedTerrain = user.getClaimedTerrain();

        if (claimedTerrain != null) {
            Terrain terrain = terrainRepository.getTerrain(
                    claimedTerrain.getTerrainId()
            );

            if (terrain != null) {
                terrainService.removeClaimed(terrain);
                regionHandler.disavowPlayers(terrain, claimedTerrain);
                Cuboid cuboid = terrain.getCuboid();

                schematicHandler.saveSchematic(
                        user.getPlayerId().toString(),
                        cuboid
                );

                schematicHandler.pasteSchematic(
                        "default", cuboid
                );
            }
        }
    }

    @Override
    public void saveAllUsers() {
        userRepository.saveAllUsers(this::saveSyncUser);
    }
}
