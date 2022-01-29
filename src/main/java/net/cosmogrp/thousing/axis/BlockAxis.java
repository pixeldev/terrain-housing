package net.cosmogrp.thousing.axis;

import com.sk89q.worldedit.math.BlockVector3;
import net.cosmogrp.thousing.codec.Codec;
import net.cosmogrp.thousing.util.DataStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class BlockAxis implements Codec {

    private String worldName;
    private int x;
    private int y;
    private int z;

    private BlockAxis(
            String worldName,
            int x, int y, int z
    ) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private BlockAxis() {
        // For codec
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

    public void move(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
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

    public static BlockAxis from(Location location) {
        return new BlockAxis(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }

    public static BlockAxis from(DataInputStream input) throws IOException {
        BlockAxis blockAxis = new BlockAxis();
        blockAxis.read(input);
        return blockAxis;
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        DataStreams.writeString(worldName, output);
        output.writeInt(x);
        output.writeInt(y);
        output.writeInt(z);
    }

    @Override
    public void read(DataInputStream input) throws IOException {
        worldName = DataStreams.readString(input);
        x = input.readInt();
        y = input.readInt();
        z = input.readInt();
    }
}
