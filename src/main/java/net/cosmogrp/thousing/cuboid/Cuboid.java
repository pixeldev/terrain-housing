package net.cosmogrp.thousing.cuboid;

import net.cosmogrp.thousing.block.BlockAxis;

public class Cuboid {

    private final BlockAxis minPoint;
    private final BlockAxis maxPoint;

    public Cuboid(BlockAxis minPoint, BlockAxis maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }

    public BlockAxis getMinPoint() {
        return minPoint;
    }

    public BlockAxis getMaxPoint() {
        return maxPoint;
    }
}
