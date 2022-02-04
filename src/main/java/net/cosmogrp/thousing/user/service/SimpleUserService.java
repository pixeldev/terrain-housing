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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import java.util.UUID;
import java.util.function.Consumer;

public class SimpleUserService implements UserService {

    @Inject private TerrainRepository terrainRepository;
    @Inject private Plugin plugin;
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
    public void authorizePlayer(
            Player sender,
            OfflinePlayer target
    ) {
        if (sender.getUniqueId().equals(target.getUniqueId())) {
            messageHandler.sendMessage(
                    sender,
                    "user.self"
            );
            return;
        }

        if (!target.hasPlayedBefore()) {
            messageHandler.sendMessage(
                    sender,
                    "user.target-not-found"
            );
            return;
        }

        getAndExecute(sender, user -> getClaimedTerrain(sender, user, terrain -> {
            Terrain realTerrain = terrainRepository.getTerrain(terrain.getTerrainId());

            if (terrain.authorizePlayer(target)) {
                regionHandler.authorizePlayer(realTerrain, target.getUniqueId());
                messageHandler.sendMessage(
                        sender,
                        "user.authorize-success",
                        "%target%", target.getName()
                );
            } else {
                messageHandler.sendMessage(
                        sender,
                        "user.already-authorized",
                        "%target%", target.getName()
                );
            }
        }));
    }

    @Override
    public void disavowPlayer(
            Player sender,
            OfflinePlayer target
    ) {
        if (sender.getUniqueId().equals(target.getUniqueId())) {
            messageHandler.sendMessage(
                    sender,
                    "user.self"
            );
            return;
        }

        if (!target.hasPlayedBefore()) {
            messageHandler.sendMessage(
                    sender,
                    "user.target-not-found"
            );
            return;
        }

        getAndExecute(sender, user -> getClaimedTerrain(sender, user, terrain -> {
            Terrain realTerrain = terrainRepository.getTerrain(terrain.getTerrainId());

            if (terrain.disavowPlayer(target)) {
                regionHandler.disavowPlayer(realTerrain, target.getUniqueId());
                messageHandler.sendMessage(
                        sender,
                        "user.disavow-success",
                        "%target%", target.getName()
                );
            } else {
                messageHandler.sendMessage(
                        sender,
                        "user.not-authorized",
                        "%target%", target.getName()
                );
            }
        }));
    }

    private void getClaimedTerrain(
            Player player, User user,
            Consumer<ClaimedTerrain> action
    ) {
        ClaimedTerrain terrain = user.getClaimedTerrain();

        if (terrain != null) {
            if (!terrain.isLoaded()) {
                messageHandler.sendMessage(
                        player,
                        "user.terrain-not-loaded"
                );
            } else {
                action.accept(terrain);
            }
        } else {
            messageHandler.sendMessage(
                    player,
                    "user.not-claimed"
            );
        }
    }

    private void getAndExecute(Player player, Consumer<User> action) {
        User user = userRepository.getUser(player);

        if (user != null) {
            action.accept(user);
        } else {
            messageHandler.sendMessage(
                    player,
                    "user.not-found"
            );
        }
    }

    @Override
    public void tryToClaim(Player player, Terrain terrain) {
        if (!player.hasPermission("terrain.claim")) {
            messageHandler.sendMessages(player, "user.no-claim-permission");
            return;
        }

        if (!terrain.isEnabled()) {
            messageHandler.sendMessage(player, "terrain.not-enabled");
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
                regionHandler.disavowPlayers(terrain, claimedTerrain);
                Cuboid cuboid = terrain.getCuboid();

                schematicHandler.saveSchematic(
                        user.getPlayerId().toString(),
                        cuboid
                );

                schematicHandler.pasteSchematic("default", cuboid);
                terrainService.removeClaimed(terrain);
            }
        }
    }

    @Override
    public void saveAllUsers() {
        userRepository.saveAllUsers(this::saveSyncUser);
    }
}
