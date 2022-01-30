package net.cosmogrp.thousing.user;

import net.cosmogrp.thousing.codec.Codec;
import net.cosmogrp.thousing.terrain.ClaimedTerrain;
import net.cosmogrp.thousing.util.DataStreams;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class User implements Codec {

    private UUID playerId;
    private ClaimedTerrain claimedTerrain;

    private User(UUID playerId) {
        this.playerId = playerId;
    }

    private User() {
        // For codec
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

    public static User from(Player player) {
        return new User(player.getUniqueId());
    }

    public static User from(DataInputStream input) throws IOException {
        User user = new User();
        user.read(input);
        return user;
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        DataStreams.writeUuid(playerId, output);
        output.writeBoolean(claimedTerrain != null);
        if (claimedTerrain != null) {
            claimedTerrain.write(output);
        }
    }

    @Override
    public void read(DataInputStream input) throws IOException {
        playerId = DataStreams.readUuid(input);
        if (input.readBoolean()) {
            claimedTerrain = ClaimedTerrain.from(input);
        }
    }
}
