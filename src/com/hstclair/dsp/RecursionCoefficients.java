package com.hstclair.dsp;

import java.util.Arrays;

/**
 * @author hstclair
 * @since 8/10/15 5:07 PM
 */
public abstract class RecursionCoefficients {
    final double[] a;
    final double[] b;

    RecursionCoefficients(int poles) {
        a = new double[poles];
        b = new double[poles];
        a[0] = 1;
        b[0] = -1;
    }

    void applyCoefficientSet(PolePairCoefficientSet coefficientSet) {
        coefficientSet.apply(a, b);
    }

    public void adjustGain() {
        double gain = computeGainAdjustment();

        for (int index = 0; index < a.length; index++)
            a[index] = a[index] / gain;
    }

    abstract double computeGainAdjustment();
}
