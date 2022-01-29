package net.cosmogrp.thousing.spatial.node;

import net.cosmogrp.thousing.spatial.mbr.MBR;
import net.cosmogrp.thousing.spatial.mbr.MBRConverter;

abstract class NodeBase<N, T> implements Node<T> {

    private MBR mbr;
    private final Object[] data;

    public NodeBase(Object[] data) {
        this.data = data;
    }

    public int size() {
        return data.length;
    }

    @SuppressWarnings("unchecked")
    public N get(int i) {
        return (N) data[i];
    }

    public MBR getMBR(MBRConverter<T> converter) {
        if (mbr == null)
            mbr = computeMBR(converter);
        return mbr;
    }

    public abstract MBR computeMBR(MBRConverter<T> converter);

    public MBR getUnion(MBR m1, MBR m2) {
        if (m1 == null)
            return m2;
        return m1.union(m2);
    }

}