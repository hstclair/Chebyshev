package com.hstclair.math;


/**
 * @author hstclair
 * @since 8/10/15 9:32 PM
 */
public class RealMobiusTransformation {

    // The Mobius Transformation represents the operation:
    // M(x) = (ax + b) / (cx + d)
    // where ad - bc is not zero

    public static final RealMobiusTransformation IDENTITY = new RealMobiusTransformation(1, 0, 0, 1);

    final double a;
    final double b;
    final double c;
    final double d;


    RealMobiusTransformation(double a, double b, double c, double d) {
        if (! validate(a, b, c, d))
            throw new IllegalArgumentException("ad - bc must be non-zero");

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    static boolean validate(double a, double b, double c, double d) {
        double ad = a * d;
        double bc = b * c;

        return ad != bc;
    }

    public double transform(double x) {
        if (x == Double.POSITIVE_INFINITY) {
            if (c == 0)
                return Double.POSITIVE_INFINITY;

            return a/c;
        }

        return (a * x + b) / (c * x + d);
    }

    public RealMobiusTransformation vincentsReduction() {
        return new RealMobiusTransformation(b, a + b, d, c + d);
    }

    public RealMobiusTransformation composeXPlusK(double k) {
        return new RealMobiusTransformation(a, a * k + b, c, c * k + d);
    }

    public RealMobiusTransformation composeAlphaX(double alpha) {
        return new RealMobiusTransformation(alpha * a, b, alpha * c, d);
    }
}
