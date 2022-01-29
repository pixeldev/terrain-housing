package net.cosmogrp.thousing.spatial.mbr;

public class SimpleMBR implements MBR {

    private final double[] values;

    private SimpleMBR(int dimensions) {
        values = new double[dimensions * 2];
    }

    public SimpleMBR(double... values) {
        this.values = values.clone();
    }

    public <T> SimpleMBR(T t, MBRConverter<T> converter) {
        int dims = converter.getDimensions();
        values = new double[dims * 2];
        int p = 0;
        for (int i = 0; i < dims; i++) {
            values[p++] = converter.getMin(i, t);
            values[p++] = converter.getMax(i, t);
        }
    }

    public int getDimensions() {
        return values.length / 2;
    }

    public double getMin(int axis) {
        return values[axis * 2];
    }

    public double getMax(int axis) {
        return values[axis * 2 + 1];
    }

    public MBR union(MBR mbr) {
        int dims = getDimensions();
        SimpleMBR n = new SimpleMBR(dims);
        int p = 0;
        for (int i = 0; i < dims; i++) {
            n.values[p] = Math.min(getMin(i), mbr.getMin(i));
            p++;
            n.values[p] = Math.max(getMax(i), mbr.getMax(i));
            p++;
        }
        return n;
    }

    public boolean intersects(MBR other) {
        for (int i = 0; i < getDimensions(); i++) {
            if (other.getMax(i) < getMin(i) || other.getMin(i) > getMax(i))
                return false;
        }
        return true;
    }

    public <T> boolean intersects(T t, MBRConverter<T> converter) {
        for (int i = 0; i < getDimensions(); i++) {
            if (converter.getMax(i, t) < getMin(i) ||
                    converter.getMin(i, t) > getMax(i))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{values: " + java.util.Arrays.toString(values) + "}";
    }

}