package com.hstclair.math.util;

import com.hstclair.math.matrix.Value;

/**
 * Created by hstclair on 4/22/17.
 */
public class DoubleFactory implements ValueFactory<Double> {

    public static final Value<Double> ZERO = new DoubleValue(0d);

    public static final Value<Double> ONE = new DoubleValue(1d);

    @Override
    public Value<Double> valueOfOne() {
        return ONE;
    }

    @Override
    public Value<Double> valueOfZero() {
        return ZERO;
    }

    @Override
    public Value<Double> valueOf(double d) {
        if (d == 0d)
            return ZERO;
        if (d == 1d)
            return ONE;

        return new DoubleValue(d);
    }

    @Override
    public Value<Double>[] vectorArray(int length) {
        return new DoubleValue[length];
    }

    @Override
    public Value<Double>[][] matrixArray(int rows, int columns) {
        if (columns == 0)
            return new DoubleValue[rows][];

        return new DoubleValue[rows][columns];
    }
}
