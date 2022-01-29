package net.cosmogrp.thousing.spatial.point;

public class SimplePointND implements PointND {

    private final double[] ords;

    public SimplePointND(double... ords) {
        this.ords = ords;
    }

    public int getDimensions() {
        return ords.length;
    }

    public double getOrd(int axis) {
        return ords[axis];
    }

}