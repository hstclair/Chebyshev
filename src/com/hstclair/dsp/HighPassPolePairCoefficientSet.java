package com.hstclair.dsp;

/**
 * @author hstclair
 * @since 8/10/15 5:36 PM
 */
public class HighPassPolePairCoefficientSet extends PolePairCoefficientSet {

    HighPassPolePairCoefficientSet(int poles, int polePairIndex, double cutoffFreq, PolePairTransformer polePairTransformer) {
        super(poles, polePairIndex, cutoffFreq, polePairTransformer);
    }

    HighPassPolePairCoefficientSet(int poles, int polePairIndex, double cutoffFreq) {
        super(poles, polePairIndex, cutoffFreq);
    }

    @Override
    double computeKValue(double w) {
        return -Math.cos(w/2 + 1/2) / Math.cos(w/2 - 1/2);
    }

    @Override
    double[] transformCoefficients(double[] coefficients) {
        coefficients[1] = -coefficients[1];
        return coefficients;
    }
}
