package net.cosmogrp.thousing.spatial.node;

import java.util.Comparator;

interface NodeComparators<T> {

    Comparator<T> getMinComparator(int axis);

    Comparator<T> getMaxComparator(int axis);

}