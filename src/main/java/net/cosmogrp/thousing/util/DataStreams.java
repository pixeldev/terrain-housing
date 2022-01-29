package net.cosmogrp.thousing.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public final class DataStreams {

    private DataStreams() {
        throw new UnsupportedOperationException();
    }

    public static void writeUuid(
            UUID value,
            DataOutputStream output
    ) throws IOException {
        output.writeLong(value.getMostSignificantBits());
        output.writeLong(value.getLeastSignificantBits());
    }

    public static UUID readUuid(
            DataInputStream input
    ) throws IOException {
        return new UUID(
                input.readLong(),
                input.readLong()
        );
    }

    public static void writeString(
            String value,
            DataOutputStream output
    ) throws IOException {
        output.writeInt(value.length());
        output.writeChars(value);
    }

    public static String readString(
            DataInputStream input
    ) throws IOException {
        int length = input.readInt();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(input.readChar());
        }
        return builder.toString();
    }

}
