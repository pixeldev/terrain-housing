package net.cosmogrp.thousing.spatial.mbr;

public interface MBR {

    int getDimensions();

    double getMin(int axis);

    double getMax(int axis);

    MBR union(MBR mbr);

    boolean intersects(MBR other);

    <T> boolean intersects(T t, MBRConverter<T> converter);

}