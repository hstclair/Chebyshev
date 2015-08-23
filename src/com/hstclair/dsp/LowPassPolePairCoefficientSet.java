package com.hstclair.dsp;

/**
 * @author hstclair
 * @since 8/10/15 5:07 PM
 */
public class LowPassPolePairCoefficientSet extends PolePairCoefficientSet {

    LowPassPolePairCoefficientSet(int poles, int polePairIndex, double cutoffFreq, PolePairTransformer polePairTransformer) {
        super(poles, polePairIndex, cutoffFreq, polePairTransformer);
    }

    LowPassPolePairCoefficientSet(int poles, int polePairIndex, double cutoffFreq) {
        super(poles, polePairIndex, cutoffFreq);
    }

    @Override
    double computeKValue(double cutoffFreq) {
        double radians = 2 * Math.PI * cutoffFreq;
        return Math.sin(1/2 - radians/2) / Math.sin(1/2 + radians/2);
    }

    @Override
    double[] transformCoefficients(double[] coefficients) {
        return coefficients;
    }
}
