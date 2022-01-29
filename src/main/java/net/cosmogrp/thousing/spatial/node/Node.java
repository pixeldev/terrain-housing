package net.cosmogrp.thousing.spatial.node;

import net.cosmogrp.thousing.spatial.dist.DistanceCalculator;
import net.cosmogrp.thousing.spatial.dist.DistanceResult;
import net.cosmogrp.thousing.spatial.mbr.MBR;
import net.cosmogrp.thousing.spatial.mbr.MBRConverter;

import java.util.List;
import java.util.PriorityQueue;

interface Node<T> {

    int size();

    MBR getMBR(MBRConverter<T> converter);

    void expand(MBR mbr, MBRConverter<T> converter,
                List<T> found, List<Node<T>> nodesToExpand);

    void find(MBR mbr, MBRConverter<T> converter, List<T> result);

    void nnExpand(DistanceCalculator<T> dc,
                  NodeFilter<T> filter,
                  List<DistanceResult<T>> currentFinds,
                  int maxHits,
                  PriorityQueue<Node<T>> queue,
                  MinDistComparator<T, Node<T>> mdc);

}