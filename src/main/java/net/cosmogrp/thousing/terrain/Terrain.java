package net.cosmogrp.thousing.terrain;

import net.cosmogrp.thousing.block.BlockAxis;
import net.cosmogrp.thousing.codec.Codec;
import net.cosmogrp.thousing.cuboid.Cuboid;
import net.cosmogrp.thousing.util.DataStreams;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Terrain implements Codec {

    private String id;

    private BlockAxis skullLocation;
    private BlockAxis originLocation;
    private BlockAxis signLocation;

    private Cuboid cuboid;
    private boolean enabled;

    private Terrain(String id, BlockAxis originLocation) {
        this.id = id;
        this.originLocation = originLocation;
    }

    private Terrain() {
        // For codec
    }

    public String getId() {
        return id;
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

    public BlockAxis getSkullLocation() {
        return skullLocation;
    }

    public BlockAxis getOriginLocation() {
        return originLocation;
    }

    public BlockAxis getSignLocation() {
        return signLocation;
    }

    public void setSkullLocation(BlockAxis skullLocation) {
        this.skullLocation = skullLocation;
    }

    public void setOriginLocation(BlockAxis originLocation) {
        this.originLocation = originLocation;
    }

    public void setSignLocation(BlockAxis signLocation) {
        this.signLocation = signLocation;
    }

    public static Terrain from(String id, Block originBlock) {
        return new Terrain(id, BlockAxis.from(
                originBlock.getLocation()
        ));
    }

    public static Terrain from(DataInputStream input) throws IOException {
        Terrain terrain = new Terrain();
        terrain.read(input);
        return terrain;
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        DataStreams.writeString(id, output);
        skullLocation.write(output);
        originLocation.write(output);
        signLocation.write(output);
        cuboid.write(output);
        output.writeBoolean(enabled);
    }

    @Override
    public void read(DataInputStream input) throws IOException {
        id = DataStreams.readString(input);
        skullLocation = BlockAxis.from(input);
        originLocation = BlockAxis.from(input);
        signLocation = BlockAxis.from(input);
        cuboid = Cuboid.from(input);
        enabled = input.readBoolean();
    }
}
