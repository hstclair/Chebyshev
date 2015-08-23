package com.hstclair.dsp;

import java.util.Arrays;

/**
 * @author hstclair
 * @since 8/10/15 2:57 PM
 */
public class ButterworthCoefficientCalculator {



    ButterworthCoefficientCalculator() {
    }

    public void computeLowpassCoefficients(int poles, double cutoffFreq) {

        if ((poles & 1) != 0) poles++;

        RecursionCoefficients recursionCoefficients = new LowPassRecursionCoefficients(poles);

        for (int polePair = 0; polePair < poles / 2; polePair++) {
            LowPassPolePairCoefficientSet coefficientSet = new LowPassPolePairCoefficientSet(poles, polePair, cutoffFreq);

            recursionCoefficients.applyCoefficientSet(coefficientSet);
        }
    }

}
