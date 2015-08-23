package com.hstclair.dsp;

import org.omg.PortableServer.POA;

/**
 * @author hstclair
 * @since 8/10/15 5:20 PM
 */
public abstract class PolePairCoefficientSet {

    final double a[];
    final double b[];

    final PolePairTransformer polePairTransformer;

    PolePairCoefficientSet(int poles, int polePairIndex, double cutoffFreq) {
        this(poles, polePairIndex, cutoffFreq, new NullPolePairTransformer());
    }

    PolePairCoefficientSet(int poles, int polePairIndex, double cutoffFreq, PolePairTransformer polePairTransformer) {

        this.polePairTransformer = polePairTransformer;

        double radians = (.5 + polePairIndex) * Math.PI / poles;

        PolePair polePair = new PolePair(-Math.cos(radians), Math.sin(radians));

        polePair = this.polePairTransformer.transform(polePair, poles);

        double[] x = computeXValues(polePair);
        double[] y = computeYValues(polePair);

        a = transformCoefficients(mapXYtoA(x, y, cutoffFreq));
        b = transformCoefficients(mapYtoB(y, cutoffFreq));
    }

    double[] computeXValues(PolePair polePair) {
        double t = 2 * Math.tan(.5);
        double tsqr = Math.pow(t, 2);
        double mtsqr = (Math.pow(polePair.realPole, 2) + Math.pow(polePair.imaginaryPole, 2)) * tsqr;
        double d = 4 - 4 * polePair.realPole * t + mtsqr;

        double[] x = new double[3];

        x[2] = tsqr / d;
        x[1] = 2 * x[2];
        x[0] = x[2];

        return x;
    }

    double[] computeYValues(PolePair polePair) {
        double t = 2 * Math.tan(.5);
        double tsqr = Math.pow(t, 2);
        double mtsqr = (Math.pow(polePair.realPole, 2) + Math.pow(polePair.imaginaryPole, 2)) * tsqr;
        double d = 4 - 4 * polePair.realPole * t + mtsqr;

        double[] y = new double[3];

        y[2] = 1;
        y[1] = (8 - 2 * mtsqr) / d;
        y[0] = (-4 - 4 * polePair.realPole * t - mtsqr) / d;

        return y;
    }

    double[] mapXYtoA(double[] x, double[] y, double cutoffFreq) {
        double k = computeKValue(cutoffFreq);
        double d = y[2] + y[1] * k - y[0] * Math.pow(k, 2);

        double[] a = new double[3];

        a[2] = (x[2] - x[1] * k + x[0] * Math.pow(k, 2)) / d;
        a[1] = (-2 * x[2] * k + x[1] * (1 + Math.pow(k, 2)) - 2 * x[0] * k)/d;
        a[0] = (x[2] * Math.pow(k, 2) - x[1] * k + x[0]) / d;

        return a;
    }

    double[] mapYtoB(double[] y, double cutoffFreq) {
        double k = computeKValue(cutoffFreq);
        double d = y[2] + y[1] * k - y[1] * Math.pow(k, 2);

        double[] b = new double[3];

        b[2] = 1;
        b[1] = (2 * y[2] * k + y[1] * (1 + Math.pow(k, 2)) - 2 * y[0] * k)/d;
        b[0] = (-y[2] * Math.pow(k, 2) - y[1] * k + y[0]) / d;

        return b;
    }

    public void apply(double[] a, double[] b) {
        int size = Math.min(a.length, b.length);

        for (int index = size - 1; index >= 0; index--) {
            a[index] = this.a[2] * a[index] +
                            (index > 0 ? this.a[1] * a[index - 1] : 0) +
                            (index > 1 ? this.a[0] * a[index - 2] : 0);
            b[index] = this.b[2] * b[index] -
                            (index > 0 ? this.b[1] * b[index - 1] : 0) -
                            (index > 1 ? this.b[0] * b[index - 2] : 0);
        }
    }

    abstract double computeKValue(double w);

    abstract double[] transformCoefficients(double[] coefficients);
}
