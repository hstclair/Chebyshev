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
    static final double defaultStrzebońskiAlpha = 4;

    public final double strzebońskiAlpha;

    public final Polynomial polynomial;
    public final RealMobiusTransformation mobius;

    VASComputationExperimental(Polynomial polynomial, RealMobiusTransformation mobius, double strzebońskiAlpha) {
        this.polynomial = polynomial;
        this.mobius = mobius;
        this.strzebońskiAlpha = strzebońskiAlpha;
    }

    VASComputationExperimental(Polynomial polynomial, RealMobiusTransformation mobius) {
        this(polynomial, mobius, defaultStrzebońskiAlpha);
    }


    @Override
    public boolean complete() {
        return false;
    }

    public List<VASOperation> evaluate() {
        List<Interval> roots = new LinkedList<>();
        List<VASOperation> operations = new LinkedList<>();

        Polynomial polynomial = this.polynomial;
        RealMobiusTransformation mobius = this.mobius;

        // Operations:
        // (1) Reduce Degree if Const == 0
        // (2) Test Sign Changes
        // (3) Adjust to Lower Bound
        // (4) Align Lower Bound With X == 0
        // (5) Bisect at X==1

        // (1)
        // if the constant portion of the current polynomial is zero, we have found one of the roots and
        // it's value is precisely M(0).   Add this root to the list then reduce the degree of the polynomial by
        // dividing by X (of course, this will never result in a non-zero a/x term because we've already determined the
        // constant term to be zero).
        if (polynomial.constant() == 0) {
            // add [b/d, b/d] to rootlist,
            roots.add(new Interval(mobius.transform(0)));

            // and set p(x) ← p(x)/x
            operations.add(createOperation(polynomial.reduceDegree(polynomial.lowestDegree()), mobius));

            return buildResult(roots, operations);
        }


        // (2)
        // Count the sign changes in our polynomial.  There must be at least one sign change if the polynomial has
        // any remaining positive roots.
        int signChanges = polynomial.signChanges();

        if (signChanges == 0) { // If there are no sign changes, report our results
            return buildResult(roots, operations);
        } else if (signChanges == 1) {
            // If s = 1 add intrv(a, b, c, d) to rootlist and go to Step 2.

            roots.add(intervalOf(mobius));

            return buildResult(roots, operations);
        }

        // (3)
        // Compute a lower bound α ∈ Z on the positive roots of p.
        double lowerBoundComputed = lowerBound(polynomial);

//        // if the lower bound of the polynomial's roots is greater than the polynomial's constant term then scale the
//        // polynomial (and the associated Mobius Transformation) so that this lower bound coincides with x=1
//        //
//        //  If α > α0 set p(x) ← p(αx), a ← αa, c ← αc, and α ← 1
//        if (lowerBoundComputed > strzebońskiAlpha) {       // Strzeboński's contribution
//
//            System.out.printf("Lower bound is greater than %f.  Transforming polynomial so that lower bound is exactly 1\n", strzebońskiAlpha);
//
//            polynomial = polynomial.apply(Polynomial.of(new double[] { 0, lowerBoundComputed }));
//            mobius = mobius.composeAlphaX(lowerBoundComputed);
//
//            lowerBoundComputed = 1;
//        }

        // (4)
        // if the lower bound is greater than or equal to 1, compose the polynomial with (x + lowerBound) so that
        // the lower bound now coincides with zero
        //
        // If α ≥ 1, set p(x) ← p(x + α), b ← αa + b, and d ← αc + d
        if (lowerBoundComputed >= 1) {
            Polynomial composed = Polynomial.of(new double[]{lowerBoundComputed, 1});

            polynomial = polynomial.apply(composed);
            mobius = mobius.composeXPlusK(lowerBoundComputed);

            operations.add(createOperation(polynomial, mobius));

            return buildResult(roots, operations);
        }

        // prior transformations ensure there are now one or more roots in (0, 1) and one or more roots in (1, infinity)
        // (5)
        // Prepare for identification of roots in interval (1, infinity):
        // Use Taylor shift polynomial so that roots within (1, infinity) now lie within interval (0, infinity)
        // then re-apply VAS
        // Compute p1(x) ← p(x + 1)
        Polynomial polynomial1 = polynomial.apply(xPlusOne);
        // set a1 ← a, b1 ← a + b, c1 ← c, d1 ← c + d
        RealMobiusTransformation mobius1 = mobius.composeXPlusK(1);

        operations.add(createOperation(polynomial1, mobius1));

        // Prepare for identification of roots within interval (0, 1)
        // Use Budan's Theorem to transform polynomial then re-apply VAS
        // a2 ← b, b2 ← a + b, c2 ← d, and d2 ← c + d
        RealMobiusTransformation mobius2 = mobius.budansTheorem();
        Polynomial polynomial2 = polynomial.budansTheorem();

        operations.add(createOperation(polynomial2, mobius2));

        return buildResult(roots, operations);
    }

    double lowerBound(Polynomial polynomial) {
        return lowerBoundEstimator.estimateLowerBound(polynomial);
    }

    double idealLowerBound(Polynomial polynomial) {
        return (int) lowerBound(polynomial);
    }

    List<VASOperation> buildResult(List<Interval> roots, List<VASOperation> operations) {
        operations.add(new VASResult(roots));

        return operations;
    }

    VASComputationExperimental createOperation(Polynomial polynomial, RealMobiusTransformation mobius) {
        return new VASComputationExperimental(polynomial, mobius, strzebońskiAlpha);
    }

    @Override
    public List<Interval> getResults() {
        return null;
    }

    Interval intervalOf(RealMobiusTransformation mobius) {
        return new Interval(Math.min(mobius.transform(0), mobius.transform(Double.POSITIVE_INFINITY)), Math.max(mobius.transform(0), mobius.transform(Double.POSITIVE_INFINITY)));
    }
}
