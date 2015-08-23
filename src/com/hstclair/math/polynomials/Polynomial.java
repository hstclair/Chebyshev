package com.hstclair.math.polynomials;

import com.hstclair.math.Complex;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a Polynomial in positive integer exponents
 *
 * @author hstclair
 * @since 8/16/15 1:59 PM
 */
public class Polynomial {
    public static final Polynomial IDENTITY = new Polynomial(new double[] {1});

    static final double[] EMPTY = new double[0];

    public static final Polynomial ZERO = new Polynomial(EMPTY);

    double[] coefficients;

    /**
     * @param coefficients the coefficients of the polynomial alpha 1 * X ^ k1 + alpha 2 * X ^ k2 + ...
     *                     organized such that:
     *                     1. coefficients[0] = alpha max(k1,k2,k3...) -- <i>the "first" element is the leading coefficient</i>
     *                     2. <i>each</i> coefficient is represented, including <b>0</b> value coefficients
     */
    Polynomial(double[] coefficients) {
        this.coefficients = trimCoefficients(coefficients);
    }

    Polynomial(double coefficient) {
        if (coefficient == 0)
            coefficients = EMPTY;
        else
            coefficients = new double[] {coefficient};
    }

    public Polynomial product(Polynomial multiplicand) {
        Objects.requireNonNull(multiplicand);

        if (this == IDENTITY)
            return multiplicand;

        if (multiplicand == IDENTITY)
            return this;

        if (this == ZERO || multiplicand == ZERO)
            return ZERO;

        if (degree() == 0)
            return multiplicand.product(coefficients[0]);

        if (multiplicand.degree() == 0)
            return product(multiplicand.getCoefficients()[0]);

        double[] newCoefficients = new double[degree() + multiplicand.degree() + 1];

        for (int innerTerm = 0; innerTerm <= degree(); innerTerm++) {
            for (int outerTerm = 0; outerTerm <= multiplicand.degree(); outerTerm++)
                newCoefficients[innerTerm + outerTerm] += coefficients[innerTerm] * multiplicand.coefficients[outerTerm];
        }

        return Polynomial.of(newCoefficients);
    }

    public Polynomial product(double multiplicand) {
        if (multiplicand == 0)
            return ZERO;

        if (multiplicand == 1)
            return this;

        if ((this.coefficients == EMPTY) || this.coefficients.length == 0)
            return ZERO;

        if (this == IDENTITY)
            return Polynomial.of(multiplicand);

        double[] newCoefficients = new double[degree() + 1];

        for (int index = 0; index <= degree(); index++) {
            newCoefficients[index] = coefficients[index] * multiplicand;
        }

        return Polynomial.of(newCoefficients);
    }

    public Polynomial sum(Polynomial addend) {
        Objects.requireNonNull(addend);

        if (addend == ZERO)
            return this;

        if (this == ZERO)
            return addend;

        double[] newCoefficients = new double[Math.max(degree(), addend.degree()) + 1];

        for (int index = 0; index <= degree(); index++)
            newCoefficients[index] = coefficients[index];

        for (int index = 0; index <= addend.degree(); index++) {
            newCoefficients[index] += addend.coefficients[index];
        }

        return Polynomial.of(newCoefficients);
    }

    public Polynomial sum(double addend) {
        double[] newCoefficients = coefficients.clone();

        newCoefficients[0] += addend;

        return Polynomial.of(newCoefficients);
    }

    public Polynomial difference(Polynomial subtrahend) {
        Objects.requireNonNull(subtrahend);

        if (subtrahend == ZERO)
            return this;

        if (this == ZERO)
            return subtrahend.negate();

        double[] newCoefficients = new double[Math.max(degree(), subtrahend.degree()) + 1];

        for (int index = 0; index <= degree(); index++)
            newCoefficients[index] = coefficients[index];

        for (int index = 0; index <= subtrahend.degree(); index++) {
            newCoefficients[index] -= subtrahend.coefficients[index];
        }

        return Polynomial.of(newCoefficients);
    }

    /**
     * return a polynomial constructed by raising this polynomial to the specified power
     *
     * @param exponent
     * @return
     */
    public Polynomial power(int exponent) {

        if (exponent < 0)
            throw new IllegalArgumentException("exponent must be positive");

        if (exponent == 0)
            return IDENTITY;

        if (exponent == 1)
            return this;

        Polynomial value = this;

        for (int count = 1; count < exponent; count++) {
            value = value.product(this);
        }

        return value;
    }

    /**
     * return the additive inverse of this polynomial
     * @return
     */
    public Polynomial negate() {
        double[] coefficients = this.coefficients.clone();

        for (int index = 0; index < coefficients.length; index++)
            coefficients[index] = -coefficients[index];

        return Polynomial.of(coefficients);
    }

    /**
     * Apply this polynomial to a double value
     *
     * @param x
     * @return
     */
    public double apply(double x) {
        double result;

        if (this == ZERO)
            return 0;

        if (x == 0 || degree() == 0)
            return coefficients[0];

        if (x == 1)
            return sumOfCoefficients();

        // use Horner's Rule:
        result = coefficients[degree()] * x;

        for (int index = degree() - 1; index > 0; index--) {
            result = (result + coefficients[index]) * x;
        }

        result += coefficients[0];

        return result;
    }

    /**
     * Apply this polynomial to a complex number.
     *
     * @param complex
     * @return
     */
    public Complex apply(Complex complex) {

        Objects.requireNonNull(complex);

        if (this == ZERO)
            return Complex.ZERO;

        if (complex == Complex.ZERO || degree() == 0)
            return Complex.of(coefficients[0]);

        if (complex == Complex.ONE)
            return Complex.of(sumOfCoefficients());

        // use Horner's Rule:
        Complex result = complex.product(coefficients[degree()]);

        for (int index = degree() - 1; index > 0; index--) {
            result = result.sum(coefficients[index]).product(complex);
        }

        return result.sum(coefficients[0]);
    }

    /**
     * Apply this polynomial to another polynomial expression.
     * This will effectively return a polynomial representing the substitution of <i>expression</i> for the variable
     * in the polynomial.
     *
     * e.g. given  <b>f(x) = x^2 + 2x + 1</b> and a substitute expression of <b>x - 1</b>, the result will be:
     *   <b>f(x) = x^2 + 1 + 2x -2 + 1</b>  <i>or</i>
     *   <b>f(x) = x^2 + 2x</b>
     *
     * @param polynomial
     * @return
     */
    public Polynomial apply(Polynomial polynomial) {
        Objects.requireNonNull(polynomial);

        if (this == ZERO)
            return ZERO;

        if (polynomial == ZERO || degree() == 0)
            return Polynomial.of(coefficients[0]);

        if (polynomial == IDENTITY)
            return Polynomial.of(sumOfCoefficients());

        // use Horner's Rule:
        Polynomial result = polynomial.product(coefficients[degree()]);

        for (int index = degree() - 1; index > 0; index--) {
            result = result.sum(coefficients[index]).product(polynomial);
        }

        return result.sum(coefficients[0]);
    }

    double sumOfCoefficients() {
        double result = 0;

        for (double coefficient : coefficients)
            result += coefficient;

        return result;
    }

    public Polynomial derivative() {
        if (degree() <= 0)
            return ZERO;

        double[] newCoefficients = new double[coefficients.length - 1];

        for (int index = 0; index < newCoefficients.length; index++) {
            newCoefficients[index] = coefficients[index+1] * (index+1);
        }

        return Polynomial.of(newCoefficients);
    }

    public Polynomial integral() {
        if (degree() < 0)
            return ZERO;

        double[] newCoefficients = new double[coefficients.length + 1];

        for (int index = 1; index < newCoefficients.length; index++) {
            newCoefficients[index] = coefficients[index-1] / index;
        }

        return Polynomial.of(newCoefficients);
    }

    public static Polynomial sigma(Function<Integer, Polynomial> function, int rangeStart, int rangeEnd, int increment) {
        Objects.requireNonNull(function);

        if (increment <= 0)
            throw new IllegalArgumentException("Increment must be positive");

        List<double[]> resultList = new LinkedList<>();

        if (rangeStart > rangeEnd)
            return ZERO;

        for (int argument = rangeStart; argument <= rangeEnd; argument += increment) {
            Polynomial result = function.apply(argument);
            resultList.add(result.getCoefficients());
        }

        return Polynomial.of(Polynomial.sumOfCoefficientArrays(resultList.toArray(new double[0][])));
    }

    public static Polynomial sigma(Function<Integer, Polynomial> function, int rangeStart, int rangeEnd) {
        return sigma(function, rangeStart, rangeEnd, 1);
    }

    public static Polynomial sigma(Function<Integer, Polynomial> function, int rangeEnd) {
        return sigma(function, 0, rangeEnd, 1);
    }

    /**
     * returns a polynomial representing (x+1)^n
     * @param degree
     * @return
     */
    public static Polynomial pascal(int degree) {
        if (degree == 0) return IDENTITY;

        if (degree < 0) throw new IllegalArgumentException("degree must be positive");

        double[] coefficients = new double[degree+1];

        coefficients[0] = 1;

        for (int index = 0; index < degree; index++) {
            coefficients[index+1] = coefficients[index] * (degree - index) / (index+1);
        }

        return Polynomial.of(coefficients);
    }

    /**
     * returns the equivalent of:
     *
     *   f(1/(x+1)) * (x + 1)^degree(f(x))
     *
     * @return
     */
    public Polynomial vincentsReduction() {
        Function<Integer, Polynomial> function = (k) -> pascal(degree() - k).product(coefficients[k]);

        return Polynomial.sigma(function, degree());
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public int degree() {
        return coefficients.length - 1;
    }

    public double constant() {
        if (this == ZERO)
            return 0;       // should this instead be undefined???  Based on the rest of implementation, I think ZERO is correct...

        return coefficients[0];
    }

    public Polynomial reduceDegree() {
        if (degree() <= 0)
            return ZERO;

        double[] newCoefficients = Arrays.copyOfRange(coefficients, 1, coefficients.length - 1);

        return Polynomial.of(newCoefficients);
    }

    /**
     * Compute the number of sign changes using Descartes' Rule of Signs
     *
     * @return the number of sign changes in this polynomial
     */
    public int signChanges() {
        int count = -1;
        double lastSign = 0;

        for (double coefficient : coefficients) {
            if ((coefficient == 0) || (lastSign < 0 && coefficient < 0) || (lastSign > 0 && coefficient > 0))
                continue;

            count++;

            lastSign = coefficient;
        }

        return Math.max(count, 0);
    }

    public static Polynomial of(double coefficient) {
        if (coefficient == 0)
            return ZERO;

        if (coefficient == 1)
            return IDENTITY;

        return new Polynomial(coefficient);
    }

    public static Polynomial of(double[] coefficients) {
        Objects.requireNonNull(coefficients);

        coefficients = trimCoefficients(coefficients);

        if (coefficients.length == 0)
            return ZERO;

        if (coefficients.length == 1 && coefficients[0] == 1)
            return IDENTITY;

        return new Polynomial(coefficients);
    }

    public static double[] sumOfCoefficientArrays(double[][] coefficientArrays) {
        Objects.requireNonNull(coefficientArrays);

        int length = 0;

        for (int index = 0; index < coefficientArrays.length; index++) {
            length = Math.max(length, coefficientArrays[index].length);
        }

        double[] newCoefficients = new double[length];

        for (int termIndex = 0; termIndex < newCoefficients.length; termIndex++) {
            double sum = 0;

            for (int addendIndex = 0; addendIndex < coefficientArrays.length; addendIndex++) {
                if (coefficientArrays[addendIndex].length > termIndex)
                    sum += coefficientArrays[addendIndex][termIndex];
            }

            newCoefficients[termIndex] = sum;
        }

        return newCoefficients;
    }

    public static double[] trimCoefficients(double[] coefficients) {
        Objects.requireNonNull(coefficients);

        int zeros = 0;

        if (coefficients.length == 0)
            return coefficients;

        if (coefficients[coefficients.length - 1] != 0)
            return coefficients;

        for (int index = coefficients.length - 1; index >= 0; index--) {
            if (coefficients[index] == 0)
                zeros++;
            else
                break;
        }

        if (zeros == coefficients.length)
            return EMPTY;

        return Arrays.copyOf(coefficients, coefficients.length - zeros);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coefficients);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other.getClass() != Polynomial.class)
            return false;

        Polynomial otherPoly = (Polynomial) other;

        if (coefficients == otherPoly.coefficients)
            return true;

        return Arrays.equals(otherPoly.getCoefficients(), getCoefficients());
    }

}
