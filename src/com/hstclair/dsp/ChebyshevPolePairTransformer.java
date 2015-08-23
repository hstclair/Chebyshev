package com.hstclair.dsp;

/**
 * @author hstclair
 * @since 8/10/15 6:21 PM
 */
public class ChebyshevPolePairTransformer implements PolePairTransformer {
    final double percentRipple;

    public ChebyshevPolePairTransformer(double percentRipple) {
        this.percentRipple = percentRipple;
    }

    @Override
    public PolePair transform(PolePair polePair, int poles) {
        final double es = 1/Math.sqrt(100 / Math.pow(100 - percentRipple, 2) - 1);
        final double essqr = Math.pow(es, 2);

        final double pvx = Math.exp(Math.log(es + Math.sqrt(essqr + 1)) / poles);
        final double pkx = Math.exp(Math.log(es + Math.sqrt(essqr - 1)) / poles);

        final double kx = (pkx + 1/pkx) / 2;

        return new PolePair(polePair.realPole * ((pvx - 1/pvx) / 2) / kx, polePair.imaginaryPole * ((pvx + 1/pvx) / 2) / kx);
    }
}
