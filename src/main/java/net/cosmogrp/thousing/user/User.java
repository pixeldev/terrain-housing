package net.cosmogrp.thousing.user;

import net.cosmogrp.thousing.terrain.ClaimedTerrain;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class User {

    private final UUID playerId;
    private ClaimedTerrain claimedTerrain;

    public User(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public @Nullable ClaimedTerrain getClaimedTerrain() {
        return claimedTerrain;
    }

    public void setClaimedTerrain(ClaimedTerrain claimedTerrain) {
        this.claimedTerrain = claimedTerrain;
    }
}
