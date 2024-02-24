package com.tsdb.tsfile.encoding.fire;

public class IntFire extends Fire<Integer> {

    public IntFire(int learning_rate) {
        super(learning_rate);
        bitWidth = 8;
        accumulator = 0;
        delta = 0;
    }

    public void reset() {
        accumulator = 0;
        delta = 0;
    }

    @Override
    public Integer predict(Integer value) {
        // calculate the parameter alpha
        int alpha = accumulator >> learnShift;
        // calculate the Incremental of last value in current prediction
        int diff = (alpha * delta) >> bitWidth;
        return value + diff;
    }

    @Override
    public void train(Integer pre, Integer val, Integer err) {
        // update the gradient of learning machine.
        int gradient = err > 0 ? -delta : delta;
        accumulator -= gradient;
        delta = val - pre;
    }
}
