package com.hstclair.math.util;

import com.hstclair.math.matrix.Value;
import org.apfloat.Apfloat;

/**
 * Created by hstclair on 4/22/17.
 */
public class ApfloatFactory implements ValueFactory<Apfloat> {

    public static final ApfloatValue ONE = new ApfloatValue(Apfloat.ONE);
    public static final ApfloatValue ZERO = new ApfloatValue(Apfloat.ZERO);
    public final long precision;

    public ApfloatFactory() {
        this(1000);
    }

    public ApfloatFactory(long precision) {
        this.precision = precision;
    }

    @Override
    public Value<Apfloat> valueOf(double value) {
        if (value==0)
            return ZERO;
        if (value==1)
            return ONE;
        return new ApfloatValue(new Apfloat(value, precision));
    }

    @Override
    public Value<Apfloat> valueOfOne() {
        return ONE;
    }

    @Override
    public Value<Apfloat> valueOfZero() {
        return ZERO;
    }

    @Override
    public Value<Apfloat>[] vectorArray(int length) {
        return new ApfloatValue[length];
    }

    @Override
    public Value<Apfloat>[][] matrixArray(int rows, int columns) {
        if (columns == 0)
            return new ApfloatValue[rows][];

        return new ApfloatValue[rows][columns];
    }

}
