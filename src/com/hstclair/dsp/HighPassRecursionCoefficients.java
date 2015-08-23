package com.hstclair.dsp;

import java.util.Arrays;

/**
 * @author hstclair
 * @since 8/10/15 6:55 PM
 */
public class HighPassRecursionCoefficients extends RecursionCoefficients {


    public HighPassRecursionCoefficients(int poles) {
        super(poles);
    }

    class SignFlipper {
        boolean negateNextValue = false;

        double process(double value) {
            boolean negate = negateNextValue;

            negateNextValue = !negateNextValue;

            return (negate) ? -value : value;
        }

        void reset() { negateNextValue = false; }
    }

    @Override
    double computeGainAdjustment() {
        SignFlipper signFlipper = new SignFlipper();

        double sigmaa = Arrays.stream(a).map(signFlipper::process).sum();

        signFlipper.reset();
        double sigmab = Arrays.stream(b).map(signFlipper::process).sum();

        return sigmaa / (1 - sigmab);
    }
}
