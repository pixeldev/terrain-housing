package net.cosmogrp.thousing.terrain;

import net.cosmogrp.thousing.axis.BlockAxis;
import net.cosmogrp.thousing.axis.PlayerViewAxis;
import net.cosmogrp.thousing.codec.Codec;
import net.cosmogrp.thousing.cuboid.Cuboid;
import net.cosmogrp.thousing.util.DataStreams;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Terrain implements Codec {

    private String id;

    private PlayerViewAxis originLocation;
    private BlockAxis skullLocation;
    private BlockAxis signLocation;

    private Cuboid cuboid;
    private boolean enabled;

    private UUID claimedBy;

    private Terrain(String id, Player player) {
        this.id = id;
        this.originLocation = PlayerViewAxis.from(player);
        this.enabled = false;
    }

    private Terrain() {
        // For codec
    }

    public String getId() {
        return id;
    }

    public String getRegionId() {
        return id + "-terrain-region";
    }

    public World getWorld() {
        return originLocation.getWorld();
    }

    public @Nullable UUID getClaimedBy() {
        return claimedBy;
    }

    public void setClaimedBy(@Nullable UUID claimedBy) {
        this.claimedBy = claimedBy;
    }

    public @Nullable Cuboid getCuboid() {
        return cuboid;
    }

    public void setCuboid(Cuboid cuboid) {
        this.cuboid = cuboid;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public PlayerViewAxis getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(Player player) {
        this.originLocation = PlayerViewAxis.from(player);
    }

    public BlockAxis getSkullLocation() {
        return skullLocation;
    }

    public BlockAxis getSignLocation() {
        return signLocation;
    }

    public void setSkullLocation(Block skullBlock) {
        this.skullLocation = BlockAxis.from(skullBlock.getLocation());
    }

    public void setSignLocation(Block signBlock) {
        this.signLocation = BlockAxis.from(signBlock.getLocation());
    }

    public static Terrain from(String id, Player player) {
        return new Terrain(id, player);
    }

    public static Terrain from(DataInputStream input) throws IOException {
        Terrain terrain = new Terrain();
        terrain.read(input);
        return terrain;
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        DataStreams.writeString(id, output);
        originLocation.write(output);
        skullLocation.write(output);
        signLocation.write(output);
        cuboid.write(output);
        output.writeBoolean(enabled);
    }

    @Override
    public void read(DataInputStream input) throws IOException {
        id = DataStreams.readString(input);
        originLocation = PlayerViewAxis.from(input);
        skullLocation = BlockAxis.from(input);
        signLocation = BlockAxis.from(input);
        cuboid = Cuboid.from(input);
        enabled = input.readBoolean();
    }
}
