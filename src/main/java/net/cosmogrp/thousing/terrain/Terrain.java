package net.cosmogrp.thousing.terrain;

import net.cosmogrp.thousing.block.BlockAxis;
import net.cosmogrp.thousing.cuboid.Cuboid;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Terrain {

    private final UUID id;

    private BlockAxis skullLocation;
    private BlockAxis originLocation;
    private BlockAxis signLocation;

    private Cuboid cuboid;
    private boolean enabled;

    public Terrain(
            BlockAxis skullLocation,
            BlockAxis originLocation,
            BlockAxis signLocation
    ) {
        this.id = UUID.randomUUID();
        this.skullLocation = skullLocation;
        this.originLocation = originLocation;
        this.signLocation = signLocation;
    }

    public UUID getId() {
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

}
