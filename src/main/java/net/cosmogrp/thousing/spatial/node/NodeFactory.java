package net.cosmogrp.thousing.spatial.node;

interface NodeFactory<N> {

    N create(Object[] data);

}