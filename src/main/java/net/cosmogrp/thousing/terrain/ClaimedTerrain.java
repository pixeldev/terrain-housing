package net.cosmogrp.thousing.terrain;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class ClaimedTerrain {

    private String terrainId;

    private Set<UUID> authorizedPlayers;

    public String getTerrainId() {
        return terrainId;
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
