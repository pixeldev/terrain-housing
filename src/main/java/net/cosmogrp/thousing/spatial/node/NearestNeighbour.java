package net.cosmogrp.thousing.spatial.node;

import net.cosmogrp.thousing.spatial.dist.DistanceCalculator;
import net.cosmogrp.thousing.spatial.dist.DistanceResult;
import net.cosmogrp.thousing.spatial.mbr.MBRConverter;
import net.cosmogrp.thousing.spatial.point.PointND;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class NearestNeighbour<T> {

    private final MBRConverter<T> converter;
    private final NodeFilter<T> filter;
    private final int maxHits;
    private final Node<T> root;
    private final DistanceCalculator<T> dc;
    private final PointND p;

    public NearestNeighbour(MBRConverter<T> converter,
                            NodeFilter<T> filter,
                            int maxHits,
                            Node<T> root,
                            DistanceCalculator<T> dc,
                            PointND p) {
        this.converter = converter;
        this.filter = filter;
        this.maxHits = maxHits;
        this.root = root;
        this.dc = dc;
        this.p = p;
    }

    public List<DistanceResult<T>> find() {
        List<DistanceResult<T>> ret =
                new ArrayList<>(maxHits);
        MinDistComparator<T, Node<T>> nc =
                new MinDistComparator<>(converter, p);
        PriorityQueue<Node<T>> queue = new PriorityQueue<>(20, nc);
        queue.add(root);
        while (!queue.isEmpty()) {
            Node<T> n = queue.remove();
            n.nnExpand(dc, filter, ret, maxHits, queue, nc);
        }
        return ret;
    }

}