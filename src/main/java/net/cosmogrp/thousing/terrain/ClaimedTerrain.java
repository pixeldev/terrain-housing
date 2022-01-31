package net.cosmogrp.thousing.terrain;

import net.cosmogrp.thousing.codec.Codec;
import net.cosmogrp.thousing.util.DataStreams;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClaimedTerrain implements Codec {

    private UUID ownerId;
    private final Set<UUID> authorizedPlayers;

    private String terrainId;

    private ClaimedTerrain(String terrainId, UUID ownerId) {
        this.terrainId = terrainId;
        this.ownerId = ownerId;
        this.authorizedPlayers = new HashSet<>();
    }

    private ClaimedTerrain() {
        // For codec
        this.authorizedPlayers = new HashSet<>();
    }

    public String getTerrainId() {
        return terrainId;
    }

    public void setTerrainId(String terrainId) {
        this.terrainId = terrainId;
    }

    public boolean isLoaded() {
        return terrainId != null;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public Iterable<UUID> getAuthorizedPlayers() {
        return authorizedPlayers;
    }

    public boolean authorizePlayer(OfflinePlayer player) {
        return authorizedPlayers.add(player.getUniqueId());
    }

    public boolean disavowPlayer(OfflinePlayer player) {
        return authorizedPlayers.remove(player.getUniqueId());
    }

    public static ClaimedTerrain from(String terrainId, Player player) {
        return new ClaimedTerrain(terrainId, player.getUniqueId());
    }

    public static ClaimedTerrain from(DataInputStream in) throws IOException {
        ClaimedTerrain claimedTerrain = new ClaimedTerrain();
        claimedTerrain.read(in);
        return claimedTerrain;
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        DataStreams.writeUuid(ownerId, output);
        output.writeInt(authorizedPlayers.size());
        for (UUID uuid : authorizedPlayers) {
            DataStreams.writeUuid(uuid, output);
        }
    }

    @Override
    public void read(DataInputStream input) throws IOException {
        ownerId = DataStreams.readUuid(input);
        int size = input.readInt();
        for (int i = 0; i < size; i++) {
            authorizedPlayers.add(DataStreams.readUuid(input));
        }
    }
}
