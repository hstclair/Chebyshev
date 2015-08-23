package com.hstclair.math.polynomials;

import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 8/16/15 11:38 AM
 */
public class DecomposedCoefficientSupplier implements Supplier<Double> {

    double coefficient;

    int timesUsed;

    DecomposedCoefficientSupplier(double coefficient) {
        this.coefficient = coefficient;
        timesUsed = 0;
    }

    @Override
    public Double get() {
        timesUsed++;

        return coefficient / (1 << timesUsed);
    }
}
