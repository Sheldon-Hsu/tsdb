package com.tsdb.tsfile.encoding.fire;

public class LongFire extends Fire<Long> {
    public LongFire(int learning_rate) {
        super(learning_rate);
        bitWidth = 16;
        accumulator = 0;
        delta = 0L;
    }

    public void reset() {
        accumulator = 0;
        delta = 0L;
    }

    @Override
    public Long predict(Long value) {
        long alpha = accumulator >> learnShift;
        long diff = (alpha * delta) >> bitWidth;
        return value + diff;
    }

    @Override
    public void train(Long pre, Long val, Long err) {
        long gradient = err > 0 ? -delta : delta;
        accumulator -= gradient;
        delta = val - pre;
    }
}
