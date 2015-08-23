package com.hstclair.math;


/**
 * @author hstclair
 * @since 8/10/15 9:32 PM
 */
public class ComplexMobiusTransformation {

    // The Mobius Transformation represents the operation:
    // M(x) = (ax + b) / (cx + d)
    // where ad - bc is not zero

    final static ComplexMobiusTransformation IDENTITY = new ComplexMobiusTransformation(Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE);

    final Complex a;
    final Complex b;
    final Complex c;
    final Complex d;


    ComplexMobiusTransformation(Complex a, Complex b, Complex c, Complex d) {
        if (! validate(a, b, c, d))
            throw new IllegalArgumentException("ad - bc must be non-zero");

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    static boolean validate(Complex a, Complex b, Complex c, Complex d) {
        Complex ad = a.product(d);
        Complex bc = b.product(c);

        return ad != bc;
    }

    public double transform(double x) {
        if (x == Double.POSITIVE_INFINITY && c == Complex.ZERO)
            return Double.POSITIVE_INFINITY;

        Complex result = transform(new Complex(x));

        if (result.imaginary != 0)
            throw new IllegalStateException("result is complex!");

        return result.real;
    }

    public Complex transform(Complex x) {
        return (a.product(x).sum(b)).quotient(c.product(x).sum(d));
    }

    public ComplexMobiusTransformation vincentsReduction() {
        return new ComplexMobiusTransformation(b, a.sum(b), d, c.sum(d));
    }

    public ComplexMobiusTransformation composeXPlusK(double k) {
        return new ComplexMobiusTransformation(a, a.product(k).sum(b), c, c.product(k).sum(d));
    }
}
