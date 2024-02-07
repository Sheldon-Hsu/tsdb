package com.tsdb.common.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PublicBAOS extends ByteArrayOutputStream {

    public PublicBAOS() {
        super();
    }

    public PublicBAOS(int size) {
        super(size);
    }

    public byte[] getBuf() {

        return this.buf;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }

    public void reset() {
        count = 0;
    }

    public int size() {
        return count;
    }

    public void truncate(int size) {
        count = size;
    }

}
