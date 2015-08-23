package com.hstclair.math;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;

/**
 * @author hstclair
 * @since 8/10/15 9:32 PM
 */
public class Complex {
    final public static Complex ZERO = new Complex(0, 0);
    public final static Complex ONE = new Complex(1, 0);

    public final double real;

    public final double imaginary;

    Complex(double real) {
        this(real, 0);
    }

    Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static Complex of(double real) {
        if (real == 0) return ZERO;
        if (real == 1) return ONE;

        return new Complex(real);
    }

    public static Complex of(double real, double imaginary) {
        if (imaginary == 0) return of(real);

        return new Complex(real, imaginary);
    }

    public Complex product(Complex multiplicand) {
        return Complex.of(real * multiplicand.real - imaginary * multiplicand.imaginary, real * multiplicand.imaginary + imaginary * multiplicand.real);
    }

    public Complex product(double multiplicand) {
        return Complex.of(multiplicand * real, multiplicand * imaginary);
    }

    public Complex sum(double addend) {
        return Complex.of(addend + real, imaginary);
    }

    public Complex difference(double subtrahend) {
        return Complex.of(real - subtrahend);
    }

    public Complex quotient(double divisor) {
        return Complex.of(real / divisor, imaginary / divisor);
    }

    public Complex quotient(Complex divisor) {
        double numerator = Math.pow(divisor.real, 2) + Math.pow(divisor.imaginary, 2);

        return Complex.of((real*divisor.real + imaginary*divisor.imaginary) / numerator, (imaginary * divisor.real - real * divisor.imaginary) / numerator );
    }

    public Complex sum(Complex addend) {
        return Complex.of(real + addend.real, imaginary + addend.imaginary);
    }

    public Complex difference(Complex subtrahend) {
        return Complex.of(real - subtrahend.real, imaginary - subtrahend.imaginary);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(real);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(imaginary);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;

        if (object == null) return false;

        if (object.getClass() == Complex.class) {

            Complex other = (Complex) object;

            return (other.real == this.real && other.imaginary == this.imaginary);
        }

        if (object.getClass() == Double.class) {
            Double other = (Double) object;

            return (real == other && imaginary == 0);
        }

        return false;
    }

}
