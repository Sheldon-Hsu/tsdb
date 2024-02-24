package com.tsdb.tsfile.encoding.encode.sprintz;

import com.tsdb.tsfile.conf.TSFileConfig;
import com.tsdb.tsfile.conf.TSFileDescriptor;
import com.tsdb.tsfile.encoding.TSEncoding;
import com.tsdb.tsfile.encoding.encode.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class SprintzEncoder extends Encoder {
    protected static final Logger logger = LoggerFactory.getLogger(SprintzEncoder.class);

    // Segment block size to compress:8
    protected int Block_size = 8;

    // group size maximum
    protected int groupMax = 16;

    // group number
    protected int groupNum;

    // the bit width used for bit-packing and rle.
    protected int bitWidth;

    /**
     * output stream to buffer {@code <bitwidth> <encoded-data>}.
     */
    protected ByteArrayOutputStream byteCache;

    // selecet the predict method
    protected String predictMethod =
            TSFileDescriptor.getInstance().getConf().getSprintzPredictScheme();

    protected boolean isFirstCached = false;

    protected TSFileConfig config = TSFileDescriptor.getInstance().getConf();

    protected SprintzEncoder() {
        super(TSEncoding.SPRINTZ);
        byteCache = new ByteArrayOutputStream();
    }

    protected void reset() {
        byteCache.reset();
        isFirstCached = false;
        groupNum = 0;
    }

    protected abstract void bitPack() throws IOException;
}
