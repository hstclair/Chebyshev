package com.hstclair.math.polynomials;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author hstclair
 * @since 8/20/15 6:21 PM
 */
public class LocalMaxQuadraticLowerBound {

    LocalMaxQuadraticUpperBound upperBound = new LocalMaxQuadraticUpperBound();

    public LocalMaxQuadraticLowerBound() {
    }

    public double estimateLowerBound(Polynomial polynomial) {
        double[] coefficients = polynomial.getCoefficients().clone();

        for (int low = 0, high = coefficients.length - 1; low < high; low++, high--) {
            double tmp = coefficients[low];
            coefficients[low] = coefficients[high];
            coefficients[high] = tmp;
        }

        Polynomial reversed = Polynomial.of(coefficients);

        double ub = upperBound.estimateUpperBound(reversed);

        return 1/ub;
    }
}
