package net.cosmogrp.thousing.terrain;

import net.cosmogrp.thousing.codec.Codec;
import net.cosmogrp.thousing.util.DataStreams;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClaimedTerrain implements Codec {

    private String terrainId;

    private UUID ownerId;
    private final Set<UUID> authorizedPlayers;

    private boolean loaded;

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

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public UUID getOwnerId() {
        return ownerId;
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
        DataStreams.writeString(terrainId, output);
        DataStreams.writeUuid(ownerId, output);
        output.writeInt(authorizedPlayers.size());
        for (UUID uuid : authorizedPlayers) {
            DataStreams.writeUuid(uuid, output);
        }
    }

    @Override
    public void read(DataInputStream input) throws IOException {
        terrainId = DataStreams.readString(input);
        ownerId = DataStreams.readUuid(input);
        int size = input.readInt();
        for (int i = 0; i < size; i++) {
            authorizedPlayers.add(DataStreams.readUuid(input));
        }
    }
}
