package com.hstclair.math.polynomials.roots;

import com.hstclair.math.Interval;
import com.hstclair.math.RealMobiusTransformation;
import com.hstclair.math.polynomials.Polynomial;
import com.hstclair.math.polynomials.roots.VASComputationExperimental;
import com.hstclair.math.polynomials.roots.VincentAkritasStrzeboński;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author hstclair
 * @since 8/22/15 4:30 PM
 */
public class TestVincentAkritasStrzeboński {

    @Test
    public void smokeTest() {
        Polynomial polynomial = Polynomial.of(new double[] {7, -7, 0, 1 });

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński();

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(2, results.size());
        assertEquals(1, results.get(0).a, 0);
        assertEquals(1.5, results.get(0).b, 0);
        assertEquals(1.5, results.get(1).a, 0);
        assertEquals(2, results.get(1).b, 0);
    }

    @Test
    public void smokeTestExperimentalVASOperation() {
        // X^3 - 7X + 7
        // roots at: X ~= -3.0892 X ~= 1.3569
        Polynomial polynomial = Polynomial.of(new double[] { 7, -7, 0, 1 });

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(2, results.size());
        assertEquals(1, results.get(0).a, 0);
        assertEquals(1.5, results.get(0).b, 0);
        assertEquals(1.5, results.get(1).a, 0);
        assertEquals(2, results.get(1).b, 0);
    }

    @Test
    public void vasReturnsSingleRoot() {
        // X^2 - 2X + 1
        // one root:  X = 1
        Polynomial polynomial = Polynomial.of(new double[] { 1, -2, 1 });

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(1, results.size());
        assertTrue(results.get(0).isExactValue());
        assertEquals(1, results.get(0).getExactValue(), 0);
    }

    @Test
    public void vasReturnsZeroToOneAndOneToInfinity() {

        // X^2 - 2X + .5
        // Two roots:  X = 1/2(2 - sqrt(2))  and  X = 1/2(2 + sqrt(2))
        Polynomial polynomial = Polynomial.of(new double[] { 1, -2, .5 });

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(2, results.size());
        assertEquals(1, results.get(0).a, 0);
        assertEquals(Double.POSITIVE_INFINITY, results.get(0).b, 0);
        assertEquals(0, results.get(1).a, 0);
        assertEquals(1, results.get(1).b, 0);
    }

    @Test
    public void vasReturnsZeroToInfinityRangeForFunctionWithOneSignChange() {

        // X^2 - 2X
        // Two roots:  X = 0  and  X = 2  but VAS will only find one of them (by design)
        // The roots are not found because VAS relies on Descartes' Rule of Signs to determine the number of roots
        // Since Decartes' Rule of Signs finds that there is only on sign variation between non-zero coefficients,
        // it reports that there is only one root.
        // note that it is trivial to find the remaining root because X^2 - 2X is obviously X ( X - 2 )
        Polynomial polynomial = Polynomial.of(new double[] { 1, -2, 0 });

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(1, results.size());
        assertEquals(0, results.get(0).a, 0);
        assertEquals(Double.POSITIVE_INFINITY, results.get(0).b, 0);
    }

    @Test
    public void smokeTest2ExperimentalVASOperation() {
        // X^5 - 3X^4 + 2X^3 = X^3 (X^2 - 3X + 2) = X^3 * (X - 1) * (X - 2)
        // Three roots: X = 0, X = 1, X = 2
        Polynomial polynomial = Polynomial.of(new double[] { 0, 0, 0, 2, -3, 1 });  // X^5 - 3X^4 + 2X^3

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(3, results.size());
        assertTrue(results.get(0).isExactValue());
        assertEquals(0, results.get(0).getExactValue(), 0);
        assertTrue(results.get(1).isExactValue());
        assertEquals(1, results.get(1).getExactValue(), 0);
        assertEquals(new Interval(1, Double.POSITIVE_INFINITY), results.get(2));
    }

    @Test
    public void factorEquationWithTwoRoots() {
        // X^2 - 6 X + 8
        // with roots 2, 4
        Polynomial polynomial = Polynomial.of(new double[] { 8, -6, 1 });

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(2, results.size());
        assertTrue(results.get(0).isExactValue());
        assertEquals(2, results.get(0).getExactValue(), 0);
        assertEquals(new Interval(2, Double.POSITIVE_INFINITY), results.get(1));
    }

    void assertRootsWithinResults(double[] expectedRoots, List<Interval> results) {

        for (Double expectedRoot : expectedRoots) {
            boolean found = false;

            for (Interval interval : results) {
                if (interval.contains(expectedRoot)) {
                    found = true;
                    break;
                }
            }

            if (! found)
                fail(String.format("Root %f was not found in result set", expectedRoot));
        }
    }

    @Test
    public void factorEquationWithThreeRoots() {
        // X^3 -7 X^2 + 14 X - 8
        // with roots 1, 2, 4
        Polynomial polynomial = Polynomial.of(new double[] { -8, 14, -7, 1 });
        double[] expectedRoots = new double[] { 1, 2, 4 };

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(3, results.size());
        assertRootsWithinResults(expectedRoots, results);
    }

    @Test
    public void factorEquationWithFourRoots() {
        // X^4 - 19 X^3 + 98 X^2 - 176 X + 96
        // with roots 1, 2, 4, 12
        Polynomial polynomial = Polynomial.of(new double[] { 96, -176, 98, -19, 1 });
        double[] expectedRoots = new double[] { 1, 2, 4, 12 };

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(4, results.size());
        assertRootsWithinResults(expectedRoots, results);
    }

    @Test
    public void factorEquationWithFiveRoots() {
        // X^5 - 26 X^4 + 231 X^3 - 862 X^2 + 1328 X - 672
        // with roots 1, 2, 4, 12, 7
        Polynomial polynomial = Polynomial.of(new double[] { -672, 1328, -862, 231, -26, 1 });
        double[] expectedRoots = new double[] { 1, 2, 4, 12, 7 };

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(5, results.size());
        assertRootsWithinResults(expectedRoots, results);
    }



    @Test
    public void factorEquationWithSixRoots() {
        // x^6 - 29 X^5 + 309 X^4 - 1555 X^3 + 3914 X^2 - 4656 X + 2016
        // with roots 1, 2, 3, 4, 7, 12
        Polynomial polynomial = Polynomial.of(new double[] { 2016, -4656, 3914, -1555, 309, -29, 1 });
        double[] expectedRoots = new double[] { 1, 2, 3, 4, 7, 12 };

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(6, results.size());
        assertRootsWithinResults(expectedRoots, results);
    }

    @Test
    public void factorEquationWithFifteenRoots() {
        double[] expectedRoots = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        Polynomial polynomial = Polynomial.fromRoots(expectedRoots);

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(expectedRoots.length, results.size());
        assertRootsWithinResults(expectedRoots, results);
    }

    @Test
    public void factorEquationWithTwentyTwoRoots() {
        double[] expectedRoots = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 };
        Polynomial polynomial = Polynomial.fromRoots(expectedRoots);

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(expectedRoots.length, results.size());
        assertRootsWithinResults(expectedRoots, results);
    }


    // Higher-order polynomials (those with larger roots or additional, distinct roots) appear to exceed the precision
    // of a Double so they cannot be supported in the current implementation.  To carry this implementation beyond
    // this limitation will require a BigDecimal implementation or a Rational implementation (or something more exotic)
    // For example, this test fails:
    @Ignore
    @Test
    public void factorEquationWithTwentyTwoPrimeRoots() {
        double[] expectedRoots = new double[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71 };
        Polynomial polynomial = Polynomial.fromRoots(expectedRoots);

        VincentAkritasStrzeboński vas = new VincentAkritasStrzeboński((Polynomial polynomialArg) -> new VASComputationExperimental(polynomialArg, RealMobiusTransformation.IDENTITY));

        List<Interval> results = vas.findRootIntervals(polynomial);

        assertEquals(expectedRoots.length, results.size());
        assertRootsWithinResults(expectedRoots, results);
    }
}
