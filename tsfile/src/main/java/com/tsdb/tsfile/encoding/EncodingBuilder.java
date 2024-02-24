package com.tsdb.tsfile.encoding;

import com.tsdb.common.data.DataType;
import com.tsdb.common.exception.UnSupportedDataTypeException;
import com.tsdb.tsfile.conf.TSFileConfig;
import com.tsdb.tsfile.conf.TSFileDescriptor;
import com.tsdb.tsfile.encoding.encode.Encoder;
import com.tsdb.tsfile.encoding.encode.PlainEncoder;
import com.tsdb.tsfile.encoding.encode.chimp.DoublePrecisionChimpEncoder;
import com.tsdb.tsfile.encoding.encode.chimp.IntChimpEncoder;
import com.tsdb.tsfile.encoding.encode.chimp.LongChimpEncoder;
import com.tsdb.tsfile.encoding.encode.chimp.SinglePrecisionChimpEncoder;
import com.tsdb.tsfile.encoding.encode.gorilla.DoublePrecisionEncoder;
import com.tsdb.tsfile.encoding.encode.gorilla.IntGorillaEncoder;
import com.tsdb.tsfile.encoding.encode.gorilla.LongGorillaEncoder;
import com.tsdb.tsfile.encoding.encode.gorilla.SinglePrecisionEncoder;
import com.tsdb.tsfile.encoding.encode.sprintz.DoubleSprintzEncoder;
import com.tsdb.tsfile.encoding.encode.sprintz.FloatSprintzEncoder;
import com.tsdb.tsfile.encoding.encode.sprintz.IntSprintzEncoder;
import com.tsdb.tsfile.encoding.encode.sprintz.LongSprintzEncoder;

public abstract class EncodingBuilder {

    TSFileConfig tsFileConfig = TSFileDescriptor.getInstance().getConf();


    public static EncodingBuilder getEncodingBuilder(TSEncoding type) {
        switch (type) {
            case PLAIN:
                return new Plain();
            case GORILLA:
                return new Gorilla();
            case CHIMP:
                return new Chimp();
            case SPRINTZ:
                return new Sprintz();

            default:
                throw new UnsupportedOperationException(type.toString());
        }
    }

    public abstract Encoder getEncoder(DataType type);


    /**
     * for all DataType.
     */
    public static class Plain extends EncodingBuilder {
        int maxStringLength = tsFileConfig.getMaxStringLength();

        @Override
        public Encoder getEncoder(DataType type) {
            return new PlainEncoder(type, maxStringLength);
        }


    }


    /**
     * for FLOAT, DOUBLE, INT, LONG.
     */
    public static class Chimp extends EncodingBuilder {

        @Override
        public Encoder getEncoder(DataType type) {
            switch (type) {
                case FLOAT:
                    return new SinglePrecisionChimpEncoder();
                case DOUBLE:
                    return new DoublePrecisionChimpEncoder();
                case INTEGER:
                    return new IntChimpEncoder();
                case BIGINT:
                    return new LongChimpEncoder();
                default:
                    throw new UnSupportedDataTypeException("CHIMP doesn't support data type: " + type);
            }
        }


    }

    /**
     * for FLOAT, DOUBLE, INT, LONG.
     */
    public static class Gorilla extends EncodingBuilder {

        @Override
        public Encoder getEncoder(DataType type) {
            switch (type) {
                case FLOAT:
                    return new SinglePrecisionEncoder();
                case DOUBLE:
                    return new DoublePrecisionEncoder();
                case INTEGER:
                    return new IntGorillaEncoder();
                case BIGINT:
                    return new LongGorillaEncoder();
                default:
                    throw new UnSupportedDataTypeException("GORILLA doesn't support data type: " + type);
            }
        }


    }

    /**
     * for FLOAT, DOUBLE, INT, LONG.
     */
    public static class Sprintz extends EncodingBuilder {
        @Override
        public Encoder getEncoder(DataType type) {
            switch (type) {
                case INTEGER:
                    return new IntSprintzEncoder();
                case BIGINT:
                    return new LongSprintzEncoder();
                case FLOAT:
                    return new FloatSprintzEncoder();
                case DOUBLE:
                    return new DoubleSprintzEncoder();
                default:
                    throw new UnSupportedDataTypeException("Sprintz doesn't support data type: " + type);
            }
        }


    }

}
