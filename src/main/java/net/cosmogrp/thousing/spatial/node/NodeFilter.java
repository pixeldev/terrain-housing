package net.cosmogrp.thousing.spatial.node;

public interface NodeFilter<T> {

    boolean accept(T t);

}