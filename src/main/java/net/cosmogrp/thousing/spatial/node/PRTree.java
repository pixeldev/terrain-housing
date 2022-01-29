package net.cosmogrp.thousing.spatial.node;

import net.cosmogrp.thousing.spatial.dist.DistanceCalculator;
import net.cosmogrp.thousing.spatial.dist.DistanceResult;
import net.cosmogrp.thousing.spatial.mbr.MBR;
import net.cosmogrp.thousing.spatial.mbr.MBR2D;
import net.cosmogrp.thousing.spatial.mbr.MBRConverter;
import net.cosmogrp.thousing.spatial.mbr.SimpleMBR;
import net.cosmogrp.thousing.spatial.mbr.SimpleMBR2D;
import net.cosmogrp.thousing.spatial.point.PointND;

import java.util.*;

public class PRTree<T> {

    private final MBRConverter<T> converter;
    private final int branchFactor;

    private Node<T> root;
    private int numLeafs;
    private int height;

    public PRTree(MBRConverter<T> converter, int branchFactor) {
        this.converter = converter;
        this.branchFactor = branchFactor;
    }

    public void load(Collection<? extends T> data) {
        if (root != null)
            throw new IllegalStateException("Tree is already loaded");
        numLeafs = data.size();
        LeafBuilder lb = new LeafBuilder(converter.getDimensions(), branchFactor);

        List<LeafNode<T>> leafNodes =
                new ArrayList<>(estimateSize(numLeafs));
        lb.buildLeafs(data, new DataComparators<>(converter),
                new LeafNodeFactory(), leafNodes);

        height = 1;
        List<? extends Node<T>> nodes = leafNodes;
        while (nodes.size() > branchFactor) {
            height++;
            List<InternalNode<T>> internalNodes =
                    new ArrayList<>(estimateSize(nodes.size()));
            lb.buildLeafs(nodes, new InternalNodeComparators<>(converter),
                    new InternalNodeFactory(), internalNodes);
            nodes = internalNodes;
        }
        setRoot(nodes);
    }

    private int estimateSize(int dataSize) {
        return (int) (1.0 / (branchFactor - 1) * dataSize);
    }

    private <N extends Node<T>> void setRoot(List<N> nodes) {
        if (nodes.size() == 0)
            root = new InternalNode<>(new Object[0]);
        else if (nodes.size() == 1) {
            root = nodes.get(0);
        } else {
            height++;
            root = new InternalNode<>(nodes.toArray());
        }
    }

    private class LeafNodeFactory
            implements NodeFactory<LeafNode<T>> {
        public LeafNode<T> create(Object[] data) {
            return new LeafNode<>(data);
        }
    }

    private class InternalNodeFactory
            implements NodeFactory<InternalNode<T>> {
        public InternalNode<T> create(Object[] data) {
            return new InternalNode<>(data);
        }
    }

    public MBR2D getMBR2D() {
        MBR mbr = getMBR();
        if (mbr == null)
            return null;
        return new SimpleMBR2D(mbr.getMin(0), mbr.getMin(1),
                mbr.getMax(0), mbr.getMax(1));
    }

    public MBR getMBR() {
        return root.getMBR(converter);
    }

    public int getNumberOfLeaves() {
        return numLeafs;
    }

    public boolean isEmpty() {
        return numLeafs == 0;
    }

    public int getHeight() {
        return height;
    }

    public void find(double xmin, double ymin, double xmax, double ymax,
                     List<T> resultNodes) {
        find(new SimpleMBR(xmin, xmax, ymin, ymax), resultNodes);
    }

    public void find(MBR query, List<T> resultNodes) {
        validateRect(query);
        root.find(query, converter, resultNodes);
    }

    public Iterable<T> find(double xmin, double ymin,
                            double xmax, double ymax) {
        return find(new SimpleMBR(xmin, xmax, ymin, ymax));
    }

    public Iterable<T> find(final MBR query) {
        validateRect(query);
        return () -> new Finder(query);
    }

    private void validateRect(MBR query) {
        for (int i = 0; i < converter.getDimensions(); i++) {
            double max = query.getMax(i);
            double min = query.getMin(i);
            if (max < min)
                throw new IllegalArgumentException("max: " + max +
                        " < min: " + min +
                        ", axis: " + i +
                        ", query: " + query);
        }
    }

    private class Finder implements Iterator<T> {

        private final MBR mbr;

        private final List<T> ts = new ArrayList<>();
        private final List<Node<T>> toVisit = new ArrayList<>();
        private T next;

        private int visitedNodes = 0;
        private int dataNodesVisited = 0;

        public Finder(MBR mbr) {
            this.mbr = mbr;
            toVisit.add(root);
            findNext();
        }

        public boolean hasNext() {
            return next != null;
        }

        public T next() {
            T toReturn = next;
            findNext();
            return toReturn;
        }

        private void findNext() {
            while (ts.isEmpty() && !toVisit.isEmpty()) {
                Node<T> n = toVisit.remove(toVisit.size() - 1);
                visitedNodes++;
                n.expand(mbr, converter, ts, toVisit);
            }
            if (ts.isEmpty()) {
                next = null;
            } else {
                next = ts.remove(ts.size() - 1);
                dataNodesVisited++;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Not implemented");
        }

    }

    public List<DistanceResult<T>> nearestNeighbour(DistanceCalculator<T> dc,
                                                    NodeFilter<T> filter,
                                                    int maxHits,
                                                    PointND p) {
        if (isEmpty())
            return Collections.emptyList();
        NearestNeighbour<T> nn =
                new NearestNeighbour<>(converter, filter, maxHits, root, dc, p);
        return nn.find();
    }

}