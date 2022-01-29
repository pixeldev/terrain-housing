package net.cosmogrp.thousing.block;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class BlockAxis {

    private final String worldName;
    private final int x;
    private final int y;
    private final int z;

    private BlockAxis(
            String worldName,
            int x, int y, int z
    ) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Location toLocation() {
        return new Location(getWorld(), x, y, z);
    }

    public BlockVector3 toVector3() {
        return BlockVector3.at(x, y, z);
    }

    @Override
    public String toString() {
        return "BlockAxis{" +
                "worldName='" + worldName + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockAxis blockAxis = (BlockAxis) o;
        return x == blockAxis.x &&
                y == blockAxis.y &&
                z == blockAxis.z &&
                Objects.equals(worldName, blockAxis.worldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldName, x, y, z);
    }

    public static BlockAxis fromLocation(Location location) {
        return new BlockAxis(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }

    public static BlockAxis fromString(String string) {
        String[] parts = string.split(":");
        return new BlockAxis(
                parts[0],
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3])
        );
    }
}
