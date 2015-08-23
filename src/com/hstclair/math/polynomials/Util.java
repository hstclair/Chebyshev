package com.hstclair.math.polynomials;

/**
 * @author hstclair
 * @since 8/22/15 11:13 AM
 */
public class Util {
    int[] getExponents(double[] doubles) {
        int[] result = new int[doubles.length];

        for (int index = 0; index < result.length; index++) {
            result[index] = Math.getExponent(doubles[index]);
        }

        return result;
    }

    double[] getMantissas(double[] doubles) {
        double[] result = doubles.clone();

        for (int index = 0; index < result.length; index++) {
            int exponent = Math.getExponent(doubles[index]);
            result[index] = result[index] / Math.scalb(1, exponent);
        }

        return result;
    }
}
