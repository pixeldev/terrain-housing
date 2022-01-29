package net.cosmogrp.thousing.spatial.mbr;

public interface MBRConverter<T> {

    int getDimensions();

    double getMin(int axis, T t);

    double getMax(int axis, T t);

}