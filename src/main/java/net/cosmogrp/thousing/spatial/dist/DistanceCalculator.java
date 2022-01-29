package net.cosmogrp.thousing.spatial.dist;

import net.cosmogrp.thousing.spatial.point.PointND;

public interface DistanceCalculator<T> {

    double distanceTo(T t, PointND p);

}