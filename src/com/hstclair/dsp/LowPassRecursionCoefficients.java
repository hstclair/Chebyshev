package com.hstclair.dsp;

import java.util.Arrays;

/**
 * @author hstclair
 * @since 8/10/15 5:12 PM
 */
public class LowPassRecursionCoefficients extends RecursionCoefficients {

    LowPassRecursionCoefficients(int poles) {
        super(poles);
    }

    @Override
    double computeGainAdjustment() {
        double sigmaa = Arrays.stream(a).sum();
        double sigmab = Arrays.stream(b).sum();

        return sigmaa / (1 - sigmab);
    }
}
