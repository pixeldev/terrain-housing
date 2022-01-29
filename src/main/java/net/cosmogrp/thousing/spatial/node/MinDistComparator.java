package net.cosmogrp.thousing.spatial.node;

import net.cosmogrp.thousing.spatial.dist.MinDist;
import net.cosmogrp.thousing.spatial.point.PointND;
import net.cosmogrp.thousing.spatial.mbr.MBR;
import net.cosmogrp.thousing.spatial.mbr.MBRConverter;

import java.util.Comparator;

class MinDistComparator<T, S extends Node<T>> implements Comparator<S> {

    public final MBRConverter<T> converter;
    public final PointND p;

    public MinDistComparator(MBRConverter<T> converter, PointND p) {
        this.converter = converter;
        this.p = p;
    }

    public int compare(S t1, S t2) {
        MBR mbr1 = t1.getMBR(converter);
        MBR mbr2 = t2.getMBR(converter);
        return Double.compare(MinDist.get(mbr1, p),
                MinDist.get(mbr2, p));
    }

}