package com.hstclair.math.polynomials;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author hstclair
 * @since 8/16/15 12:57 PM
 */
public class TestLocalMaxQuadraticUpperBound {

    @Test
    public void smokeTest() {
        double[] coefficients = { -1, -10, 10, 1 };

        LocalMaxQuadraticUpperBound instance = new LocalMaxQuadraticUpperBound();

        double estimate = instance.estimateUpperBound(Polynomial.of(coefficients));

        assertEquals(2d, estimate);
    }
}
