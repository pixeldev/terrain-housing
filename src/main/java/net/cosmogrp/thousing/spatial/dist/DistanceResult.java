package net.cosmogrp.thousing.spatial.dist;

public class DistanceResult<T> {

    private final T t;
    private final double dist;

    public DistanceResult(T t, double dist) {
        this.t = t;
        this.dist = dist;
    }

    public T get() {
        return t;
    }

    public double getDistance() {
        return dist;
    }

}