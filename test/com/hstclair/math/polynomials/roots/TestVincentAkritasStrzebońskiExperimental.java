package com.hstclair.math.polynomials.roots;

import com.hstclair.math.Interval;
import com.hstclair.math.polynomials.Polynomial;
import com.hstclair.math.polynomials.roots.VincentAkritasStrzeboński;
import com.hstclair.math.polynomials.roots.VincentAkritasStrzebońskiExperimental;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author hstclair
 * @since 8/22/15 4:30 PM
 */
public class TestVincentAkritasStrzebońskiExperimental {

    @Test
    public void smokeTest() {
        Polynomial polynomial = Polynomial.of(new double[] {7, -7, 0, 1 });

        VincentAkritasStrzebońskiExperimental vase = new VincentAkritasStrzebońskiExperimental();

        List<Interval> results = vase.findRootIntervals(polynomial);

        assertEquals(2, results.size());
        assertEquals(1, results.get(0).a, 0);
        assertEquals(1.5, results.get(0).b, 0);
        assertEquals(1.5, results.get(1).a, 0);
        assertEquals(2, results.get(1).b, 0);
    }
}
