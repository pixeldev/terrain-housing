package net.cosmogrp.thousing.terrain;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClaimedTerrain {

    private String terrainId;

    private final UUID ownerId;
    private final Set<UUID> authorizedPlayers;

    public ClaimedTerrain(UUID ownerId) {
        this.ownerId = ownerId;
        this.authorizedPlayers = new HashSet<>();
    }

    public String getTerrainId() {
        return terrainId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setTerrainId(String terrainId) {
        this.terrainId = terrainId;
    }

    public boolean authorizePlayer(Player player) {
        return authorizedPlayers.add(player.getUniqueId());
    }

    public boolean disavowPlayer(Player player) {
        return authorizedPlayers.remove(player.getUniqueId());
    }

    public boolean isAuthorized(Player player) {
        return authorizedPlayers.contains(player.getUniqueId());
    }

}
