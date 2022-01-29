package net.cosmogrp.thousing.axis;

import net.cosmogrp.thousing.codec.Codec;
import net.cosmogrp.thousing.util.DataStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class PlayerViewAxis implements Codec {

    private String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    private PlayerViewAxis(
            String worldName,
            double x, double y, double z,
            float yaw, float pitch
    ) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public Location toLocation() {
        return new Location(
                getWorld(), x, y, z,
                yaw, pitch
        );
    }

    @Override
    public String toString() {
        return "PlayerViewAxis{" +
                "worldName='" + worldName + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerViewAxis that = (PlayerViewAxis) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0 &&
                Float.compare(that.yaw, yaw) == 0 &&
                Float.compare(that.pitch, pitch) == 0 &&
                Objects.equals(worldName, that.worldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldName, x, y, z, yaw, pitch);
    }

    public static PlayerViewAxis from(Player player) {
        Location playerLocation = player.getLocation();
        return new PlayerViewAxis(
                playerLocation.getWorld().getName(),
                playerLocation.getX(),
                playerLocation.getY(),
                playerLocation.getZ(),
                playerLocation.getYaw(),
                playerLocation.getPitch()
        );
    }

    public static PlayerViewAxis from(DataInputStream input)
            throws IOException {
        return new PlayerViewAxis(
                DataStreams.readString(input),
                input.readDouble(),
                input.readDouble(),
                input.readDouble(),
                input.readFloat(),
                input.readFloat()
        );
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        DataStreams.writeString(worldName, output);
        output.writeDouble(x);
        output.writeDouble(y);
        output.writeDouble(z);
        output.writeFloat(yaw);
        output.writeFloat(pitch);
    }

    @Override
    public void read(DataInputStream input) throws IOException {
        worldName = DataStreams.readString(input);
        x = input.readDouble();
        y = input.readDouble();
        z = input.readDouble();
        yaw = input.readFloat();
        pitch = input.readFloat();
    }
}
