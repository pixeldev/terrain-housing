package net.cosmogrp.thousing.spatial.node;

import java.util.List;

interface NodeGetter<N> {

    N getNextNode(int maxObjects);

    boolean hasMoreNodes();

    boolean hasMoreData();

    List<? extends NodeGetter<N>> split(int lowId, int highId);

}