package net.cosmogrp.thousing.cuboid;

import net.cosmogrp.thousing.block.BlockAxis;
import net.cosmogrp.thousing.codec.Codec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Cuboid implements Codec {

    private final BlockAxis minPoint;
    private final BlockAxis maxPoint;

    private Cuboid(BlockAxis minPoint, BlockAxis maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }

    public BlockAxis getMinPoint() {
        return minPoint;
    }

    public BlockAxis getMaxPoint() {
        return maxPoint;
    }

    public static Cuboid from(DataInputStream input) throws IOException {
        BlockAxis minPoint = BlockAxis.from(input);
        BlockAxis maxPoint = BlockAxis.from(input);
        return new Cuboid(minPoint, maxPoint);
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        minPoint.write(output);
        maxPoint.write(output);
    }

    @Override
    public void read(DataInputStream input) throws IOException {
        minPoint.read(input);
        maxPoint.read(input);
    }
}
