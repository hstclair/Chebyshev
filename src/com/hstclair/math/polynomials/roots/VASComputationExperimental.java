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
public class VASComputationExperimental implements VASOperation {
    static final LocalMaxQuadraticLowerBound lowerBoundEstimator = new LocalMaxQuadraticLowerBound();
    static final Polynomial xPlusOne = Polynomial.of(new double[] { 1, 1 });

    Polynomial polynomial;
    RealMobiusTransformation mobius;

    VASComputationExperimental(Polynomial polynomial, RealMobiusTransformation mobius) {
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

        // Operations:
        // (1) Reduce Degree if Const == 0
        // (2) Test Sign Changes
        // (3) Adjust to Lower Bound
        // (4) Align Lower Bound With X == 0
        // (5) Bisect at X==1

        // (1)
        // if the constant portion of the current polynomial is zero, we have found one of the roots and
        // it's value is determined by M(0).   Add this root to the list then reduce the degree of the polynomial by
        // dividing by X (this still results in a valid polynomial because there is no constant term).
        // If p(0) = 0,
        if (polynomial.constant() == 0) {
            // add [b/d, b/d] to rootlist,
            double m = mobius.transform(0);
            Interval interval = new Interval(m);
            roots.add(interval);

            // and set p(x) ← p(x)/x
            polynomial = polynomial.reduceDegree();
        }


        // (2)
        // Compute s ← sgc(p)
        int sign = polynomial.signChanges();

        if (sign == 0) { // If s = 0 go to Step 2
            return buildResult(roots, operations);
        } else if (sign == 1) {
            // If s = 1 add intrv(a, b, c, d) to rootlist and go to Step 2.
            roots.add(intervalOf(mobius));

            return buildResult(roots, operations);
        }

        // (3)
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

        // (4)
        // if the lower bound is greater than or equal to 1, compose the polynomial with (x + lowerBound) so that
        // the lower bound now coincides with zero
        //
        // If α ≥ 1, set p(x) ← p(x + α), b ← αa + b, and d ← αc + d
        if (lowerBound >= 1) {
            Polynomial composed = Polynomial.of(new double[]{lowerBound, 1});

            polynomial = polynomial.apply(composed);
            mobius = mobius.composeXPlusK(lowerBound);

            operations.add(new VASComputationExperimental(polynomial, mobius));

            return buildResult(roots, operations);
        }

        // (5)
        // Compute p1(x) ← p(x + 1)
        Polynomial polynomial1 = polynomial.apply(xPlusOne);
        // set a1 ← a, b1 ← a + b, c1 ← c, d1 ← c + d
        RealMobiusTransformation mobius1 = mobius.composeXPlusK(1);

        operations.add(new VASComputationExperimental(polynomial1, mobius1));

        // a2 ← b, b2 ← a + b, c2 ← d, and d2 ← c + d
        RealMobiusTransformation mobius2 = mobius.vincentsReduction();
        Polynomial polynomial2 = polynomial.vincentsReduction();

        operations.add(new VASComputationExperimental(polynomial2, mobius2));

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
