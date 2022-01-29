package net.cosmogrp.thousing.spatial;

import net.cosmogrp.thousing.spatial.mbr.MBR;
import net.cosmogrp.thousing.spatial.mbr.MBRConverter;
import net.cosmogrp.thousing.spatial.mbr.SimpleMBR;
import net.cosmogrp.thousing.spatial.node.PRTree;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BukkitPRTree<T> {

    private final Executor executor;

    private final Set<T> entries;
    private PRTree<T> tree;
    private final MBRConverter<T> converter;
    private final int branchFactor;

    public BukkitPRTree(MBRConverter<T> converter, int branchFactor) {
        this.converter = converter;
        this.branchFactor = branchFactor;

        entries = new HashSet<>();
        executor = Executors.newSingleThreadExecutor();
        tree = new PRTree<>(converter, branchFactor);
    }

    public Iterable<T> find(Location location) {
        return find(location, location);
    }

    public Iterable<T> find(
            Location firstPoint,
            Location secondPoint
    ) {
        return find(getBoundingBox(firstPoint, secondPoint));
    }

    public Iterable<T> find(MBR query) {
        return tree.find(query);
    }

    public void add(T entry) {
        if (entries.add(entry)) {
            rebuild();
        }
    }

    public void remove(T entry) {
        if (entries.remove(entry)) {
            rebuild();
        }
    }

    private void rebuild() {
        executor.execute(() -> {
            PRTree<T> newTree = new PRTree<>(
                    converter,
                    branchFactor
            );

            newTree.load(entries);

            tree = newTree;
        });
    }

    public static MBR getBoundingBox(
            Location firstPoint,
            Location secondPoint
    ) {
        return new SimpleMBR(
                firstPoint.getX(),
                secondPoint.getX(),
                firstPoint.getY(),
                secondPoint.getY(),
                firstPoint.getZ(),
                secondPoint.getZ()
        );
    }
}
