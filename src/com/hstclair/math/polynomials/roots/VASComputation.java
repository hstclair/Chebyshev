package com.hstclair.math.polynomials.roots;

import com.hstclair.math.Interval;
import com.hstclair.math.RealMobiusTransformation;
import com.hstclair.math.polynomials.LocalMaxQuadraticLowerBound;
import com.hstclair.math.polynomials.Polynomial;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hstclair
 * @since 8/22/15 3:33 PM
 */
public class VASComputation implements VASOperation {
    static final LocalMaxQuadraticLowerBound lowerBoundEstimator = new LocalMaxQuadraticLowerBound();
    static final Polynomial xPlusOne = Polynomial.of(new double[] { 1, 1 });

    Polynomial polynomial;
    RealMobiusTransformation mobius;

    VASComputation(Polynomial polynomial, RealMobiusTransformation mobius) {
        this.polynomial = polynomial;
        this.mobius = mobius;
    }


    @Override
    public boolean complete() {
        return false;
    }

    public List<VASOperation> evaluate() {

        List<Interval> roots = new LinkedList<>();
        List<VASOperation> operations = new LinkedList<>();

        int sign = polynomial.signChanges();

        // Compute a lower bound α ∈ Z on the positive roots of p.
        double lowerBound = lowerBoundEstimator.estimateLowerBound(polynomial);

        // if the lower bound of the polynomial's roots is greater than the polynomial's constant term then scale the
        // polynomial (and the associated Mobius Transformation) so that this lower bound coincides with x=1
        //
        //  If α > α0 set p(x) ← p(αx), a ← αa, c ← αc, and α ← 1
        if (lowerBound > polynomial.constant()) {       // missing from Wikipedia!!!
            polynomial = polynomial.apply(Polynomial.of(new double[] { 0, lowerBound }));
            mobius = mobius.composeAlphaX(lowerBound);
            lowerBound = 1;
        }

        // if the lower bound is greater than or equal to 1, compose the polynomial with (x + lowerBound) so that
        // the lower bound now coincides with zero
        //
        // If α ≥ 1, set p(x) ← p(x + α), b ← αa + b, and d ← αc + d
        if (lowerBound >= 1) {
            Polynomial composed = Polynomial.of(new double[]{lowerBound, 1});

            polynomial = polynomial.apply(composed);
            mobius = mobius.composeXPlusK(lowerBound);

            // if the constant portion of the resulting polynomial is now zero, we have found one of the roots and
            // it's value is determined by M(0).   Add this root to the list then reduce the degree of the polynomial by
            // dividing by X (this still results in a valid polynomial because there is no constant term).
            // If p(0) = 0,
            if (polynomial.constant() == 0) {
                // add [b/d, b/d] to rootlist,
                double m = mobius.transform(0);
                Interval interval = new Interval(m);
                roots.add(interval);

                // and set p(x) ← p(x)/x
                polynomial = polynomial.reduceDegree();     // TODO:  consolidate this logic at top???

                // Compute s ← sgc(p)
                sign = polynomial.signChanges();

                if (sign == 0) { // If s = 0 go to Step 2
                    return buildResult(roots, operations);
                } else if (sign == 1) {
                    // If s = 1 add intrv(a, b, c, d) to rootlist and go to Step 2.
                    roots.add(intervalOf(mobius));

                    return buildResult(roots, operations);
                }
            }
        }

        // Compute p1(x) ← p(x + 1)
        Polynomial polynomial1 = polynomial.apply(xPlusOne);
        // set a1 ← a, b1 ← a + b, c1 ← c, d1 ← c + d
        RealMobiusTransformation mobius1 = mobius.composeXPlusK(1);

        // r ← 0
        int r = 0;

        // If p1(0) = 0
        if (polynomial1.constant() == 0) {
            // add [b1/d1, b1/d1] to rootlist
            roots.add(new Interval(mobius1.transform(0)));

            // set p1(x) ← p1(x)/x
            polynomial1 = polynomial1.reduceDegree();

            // r ← 1
            r = 1;
        }

        //  Compute s1 ← sgc(p1)
        int sign1 = polynomial1.signChanges();

        // set s2 ← s − s1 − r
        int sign2 = sign - sign1 - r;

        // a2 ← b, b2 ← a + b, c2 ← d, and d2 ← c + d
        RealMobiusTransformation mobius2 = mobius.vincentsReduction();
        Polynomial polynomial2 = Polynomial.ZERO;

        //  If s2 > 1
        if (sign2 > 1) {
            //  compute p2(x) ← (x + 1)^m * p(1/(x+1)), where m is the degree of p
            polynomial2 = polynomial.vincentsReduction();

            // If p2(0) = 0
            if (polynomial2.constant() == 0) {
                // set p2(x) ← p2(x)/x
                polynomial2 = polynomial2.reduceDegree();
            }

            //. Compute s2 ← sgc(p2)
            sign2 = polynomial2.signChanges();
        }

        // If s1 < s2, swap {a1, b1, c1, d1, p1, s1} with {a2, b2, c2, d2, p2, s2}
        if (sign1 < sign2) {
            RealMobiusTransformation tmp = mobius1;
            mobius1 = mobius2;
            mobius2 = tmp;

            Polynomial polynomialTmp = polynomial1;
            polynomial1 = polynomial2;
            polynomial2 = polynomialTmp;

            int signTmp = sign1;
            sign1 = sign2;
            sign2 = signTmp;
        }

        if (sign1 == 0) // If s1 = 0 goto Step 2
            return buildResult(roots, operations);

        if (sign1 == 1) // If s1 = 1 add intrv(a1, b1, c1, d1) to rootlist
            roots.add(intervalOf(mobius1));
        else
            operations.add(new VASComputation(polynomial1, mobius1));

        if (sign2 == 0) // If s2 = 0 goto Step 2
            return buildResult(roots, operations);

        if (sign2 == 1) // If s2 = 1 add intrv(a2, b2, c2, d2) to rootlist
            roots.add(intervalOf(mobius2));
        else
            operations.add(new VASComputation(polynomial2, mobius2));

        return buildResult(roots, operations);
    }

    List<VASOperation> buildResult(List<Interval> roots, List<VASOperation> operations) {
        operations.add(new VASResult(roots));

        return operations;
    }

    @Override
    public List<Interval> getResults() {
        return null;
    }

    Interval intervalOf(RealMobiusTransformation mobius) {
        return new Interval(Math.min(mobius.transform(0), mobius.transform(Double.POSITIVE_INFINITY)), Math.max(mobius.transform(0), mobius.transform(Double.POSITIVE_INFINITY)));
    }
}
