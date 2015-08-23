package com.hstclair.math.polynomials;

import com.hstclair.math.DescartesRuleOfSigns;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * This class implements the "Local Max" Quadratic algorithm for providing an upper bound estimate
 * for the roots of a polynomial
 *
 * @author hstclair
 * @since 8/16/15 10:58 AM
 */
public class LocalMaxQuadraticUpperBound {

    /**
     * constructor
     *
     */
    public LocalMaxQuadraticUpperBound() { }


    public double estimateUpperBound(Polynomial polynomial) {
        Objects.requireNonNull(polynomial);

        if (polynomial.degree() < 2)
            throw new IllegalArgumentException("polynomial degree must be at least 2");

        if (polynomial.getCoefficients()[polynomial.degree()] < 0)
            polynomial = polynomial.negate();

        if (polynomial.signChanges() == 0)
            throw new IllegalArgumentException("polynomial must contain at least one negative coefficient");

        Optional<Supplier<Double>>[] decomposedPositiveCoefficients = decomposePositiveCoefficients(polynomial.coefficients);

        return computeEstimate(polynomial.degree(), polynomial.getCoefficients(), decomposedPositiveCoefficients);
    }

    double computeEstimate(int degree, double[] coefficients, Optional<Supplier<Double>>[] decomposedPositiveCoefficients) {
        Double estimate = null;

        for (int index = degree - 1; index >= 0; index--) {
            if (coefficients[index] < 0) {
                if (estimate == null)
                    estimate = estimateNegativeCoefficient(index, coefficients, decomposedPositiveCoefficients);
                else
                    estimate = Math.max(estimate, estimateNegativeCoefficient(index, coefficients, decomposedPositiveCoefficients));

            }
        }

        return estimate;
    }

    double estimateNegativeCoefficient(int negativeCoefficientIndex, double[] coefficients, Optional<Supplier<Double>>[] decomposedPositiveCoefficients) {
        Double estimate = null;

        int degree = coefficients.length - 1;

        for (int index = negativeCoefficientIndex + 1; index <= degree; index++) {
            if (decomposedPositiveCoefficients[index].isPresent()) {
                if (estimate == null)
                    estimate = estimateCoefficientPair(negativeCoefficientIndex, index, coefficients, decomposedPositiveCoefficients);
                else
                    estimate = Math.min(estimate, estimateCoefficientPair(negativeCoefficientIndex, index, coefficients, decomposedPositiveCoefficients));
            }
        }

        return estimate;
    }

    /**
     * Compute the radical for a pairing of a negative coefficient with a positive coefficient
     *
     * @param negativeCoefficientIndex the array index of the negative coefficient
     * @param positiveCoefficientIndex the array index of the positive coefficient
     * @return the "Local Max" Quadratic radical for the paired coefficients
     */
    double estimateCoefficientPair(int negativeCoefficientIndex, int positiveCoefficientIndex, double[] coefficients, Optional<Supplier<Double>>[] decomposedPositiveCoefficients) {
        return Math.pow(-coefficients[negativeCoefficientIndex] / decomposedPositiveCoefficients[positiveCoefficientIndex].get().get(), positiveCoefficientIndex - negativeCoefficientIndex);
    }

    public Optional<Supplier<Double>>[] decomposePositiveCoefficients(double[] coefficients) {
        Optional<Supplier<Double>>[] result = new Optional[coefficients.length];

        for (int index = 0; index < coefficients.length; index++) {
            if (coefficients[index]<=0)
                result[index] = Optional.empty();
            else
                result[index] = Optional.of(new DecomposedCoefficientSupplier(coefficients[index]));
        }

        return result;
    }
}
