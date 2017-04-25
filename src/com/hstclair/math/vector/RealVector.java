package com.hstclair.math.vector;

import java.util.Arrays;

/**
 * Created by hstclair on 4/17/17.
 */
public class RealVector {

    private double[] values;

    public RealVector(double[] values) {
        this.values = Arrays.copyOf(values, values.length);
    }

    public int count() {
        return values.length;
    }

    public double member(int index) {
        if (index < 0)
            throw new IllegalArgumentException("index must not be negative");
        if (index >= values.length)
            throw new IllegalArgumentException("Attempt to access beyond end of array");

        return values[index];
    }
}
