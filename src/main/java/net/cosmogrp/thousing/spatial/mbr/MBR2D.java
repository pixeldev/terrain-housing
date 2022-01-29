package net.cosmogrp.thousing.spatial.mbr;

public interface MBR2D {

    double getMinX();

    double getMinY();

    double getMaxX();

    double getMaxY();

    MBR2D union(MBR2D mbr);

    boolean intersects(MBR2D other);

}