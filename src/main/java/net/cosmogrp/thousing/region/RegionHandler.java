package net.cosmogrp.thousing.region;

import net.cosmogrp.thousing.terrain.ClaimedTerrain;
import net.cosmogrp.thousing.terrain.Terrain;

import java.util.UUID;

public interface RegionHandler {

    void createRegion(Terrain terrain);

    void removeRegion(Terrain terrain);

    void authorizePlayers(
            Terrain terrain,
            ClaimedTerrain claimedTerrain
    );

    void authorizePlayer(Terrain terrain, UUID target);

    void disavowPlayers(
            Terrain terrain,
            ClaimedTerrain claimedTerrain
    );

    void disavowPlayer(Terrain terrain, UUID target);

}
