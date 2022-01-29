package net.cosmogrp.thousing.spatial.mbr;

public class SimpleMBR2D implements MBR2D {

    private final double xmin;
    private final double ymin;
    private final double xmax;
    private final double ymax;

    public SimpleMBR2D(double xmin, double ymin, double xmax, double ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{xmin: " + xmin + ", ymin: " + ymin +
                ", xmax: " + xmax + ", ymax: " + ymax + "}";
    }

    public double getMinX() {
        return xmin;
    }

    public double getMinY() {
        return ymin;
    }

    public double getMaxX() {
        return xmax;
    }

    public double getMaxY() {
        return ymax;
    }

    public MBR2D union(MBR2D other) {
        double uxmin = Math.min(xmin, other.getMinX());
        double uymin = Math.min(ymin, other.getMinY());
        double uxmax = Math.max(xmax, other.getMaxX());
        double uymax = Math.max(ymax, other.getMaxY());
        return new SimpleMBR2D(uxmin, uymin, uxmax, uymax);
    }

    public boolean intersects(MBR2D other) {
        return !(other.getMaxX() < xmin || other.getMinX() > xmax ||
                other.getMaxY() < ymin || other.getMinY() > ymax);
    }

}