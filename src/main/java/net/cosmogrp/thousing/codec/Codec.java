package net.cosmogrp.thousing.codec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Codec {

    void write(DataOutputStream output) throws IOException;

    void read(DataInputStream input) throws IOException;

}
