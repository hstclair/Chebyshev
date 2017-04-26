package com.hstclair.math.matrix;

import com.hstclair.math.util.ApfloatFactory;
import com.hstclair.math.util.DoubleFactory;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.junit.Test;

import java.math.RoundingMode;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by hstclair on 4/21/17.
 */
public class TestDeterminantLUSolver {

    @Test
    public void test3x3DeterminantDouble() {
        Double expected = -306d;

        double[][] members = new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} };

        DeterminantLUSolver<Double> instance = new DeterminantLUSolver<>(new Matrix<>(members, new DoubleFactory()));

        Double result = instance.solve();

        assertEquals(expected, result);
    }

    @Test
    public void test3x3Determinant() {
        Apfloat expected = new Apfloat(-306);

        double[][] members = new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} };

        DeterminantLUSolver<Apfloat> instance = new DeterminantLUSolver<>(new Matrix<>(members, new ApfloatFactory()));

        Apfloat result = instance.solve();

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test4x4Determinant() {
        Apfloat expected = new Apfloat(-418);

        double[][] members = new double[][] { {3, 2, -1, 4}, {2, 1, 5, 7}, {0, 5, 2, -6}, {-1, 2, 1, 0} };

        DeterminantLUSolver<Apfloat> instance = new DeterminantLUSolver<>(new Matrix<>(members, new ApfloatFactory()));

        Apfloat result = instance.solve();

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test5x5Determinant() {
        double[][] members = new double[][] {
                { -5, 36, 38, 6, 11 },
                { -33, 3, 24, 22, -9 },
                { 36, -26, -33, -43, -47 },
                { 0, 27, -28, -26, 23 },
                { 7, -39, 33, 3, -43 },
        };

        Apfloat expected = new Apfloat(-61089854);

        DeterminantLUSolver<Apfloat> instance = new DeterminantLUSolver<>(new Matrix<>(members, new ApfloatFactory()));

        Apfloat result = instance.solve();

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test6x6DeterminantDouble() {
        double[][] members = new double[][] {
                { -3, 0, -5, -4, -1, -5 },
                { -4, 2, -2, 3, 2, -3 },
                { 0, 4, 0, -5, -2, -2 },
                { -4, -4, 0, -3, 1, -3 },
                { 3, 4, -2, -1, 4, -2 },
                { -1, -5, -5, 3, -3, -5 },
        };

        Double expected = 36027d;

        DeterminantLUSolver<Double> instance = new DeterminantLUSolver<>(new Matrix<>(members, new DoubleFactory()));

        Double result = instance.solve();

        assertEquals(expected, result);
    }

    @Test
    public void test6x6Determinant() {
        double[][] members = new double[][] {
                { 3, 36, 16, 2, 38, -41 },
                { -34, -39, 1, -44, 20, 14 },
                { -9, 28, -49, 2, 26, 29 },
                { -21, -49, -42, -36, 2, -25 },
                { -29, 25, -18, 28, -17, -13 },
                { -6, -47, 19, 24, 6, 19 },
        };

        Apfloat expected = new Apfloat(40433588418l);

        DeterminantLUSolver<Apfloat> instance = new DeterminantLUSolver<>(new Matrix<>(members, new ApfloatFactory()));

        Apfloat result = instance.solve();

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test7x7DeterminantDouble() {
        double[][] members = new double[][] {
                {0,-5,-4,2,-2,-5,-5},
                {-4,-1,0,-1,-4,0,-3},
                {2,-3,2,-3,4,0,-5},
                {3,0,-4,-2,-3,-5,-5},
                {-2,-2,-2,2,-4,3,3},
                {4,0,0,0,2,-5,-3},
                {-2,-5,0,-3,-2,-5,-2},
        };

        Double expected = 90344d;

        DeterminantLUSolver<Double> instance = new DeterminantLUSolver<>(new Matrix<>(members, new DoubleFactory()));

        Double result = instance.solve();

        assertEquals(expected, result);
    }

    @Test
    public void test7x7Determinant() {
        double[][] members = new double[][] {
                {0,-5,-4,2,-2,-5,-5},
                {-4,-1,0,-1,-4,0,-3},
                {2,-3,2,-3,4,0,-5},
                {3,0,-4,-2,-3,-5,-5},
                {-2,-2,-2,2,-4,3,3},
                {4,0,0,0,2,-5,-3},
                {-2,-5,0,-3,-2,-5,-2},
        };

        Apfloat expected = new Apfloat(90344);

        DeterminantLUSolver<Apfloat> instance = new DeterminantLUSolver<>(new Matrix<>(members, new ApfloatFactory()));

        Apfloat result = instance.solve();

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test10x10Determinant() {
        double[][] members = new double[][] {
                {1,0,0,-1,-1,1,-1,0,3,-3},
                {2,3,-4,-2,2,3,-2,-4,0,4},
                {-1,-3,3,0,0,-3,4,4,-3,-2},
                {3,-3,4,3,-1,0,-2,-1,-4,1},
                {-5,-5,1,4,0,2,-2,-2,0,4},
                {-5,-5,-4,-4,1,-4,-4,-5,3,2},
                {-1,-5,-4,2,-2,-4,-4,0,0,-5},
                {1,3,-3,0,0,-5,1,-4,1,0},
                {-2,0,-5,4,3,3,-5,1,4,-2},
                {-2,-2,-2,-5,0,4,-3,-3,3,-5},
        };
        Apfloat expected = new Apfloat(-39656706);

        DeterminantLUSolver<Apfloat> instance = new DeterminantLUSolver<>(new Matrix<>(members, new ApfloatFactory()));

        Apfloat result = instance.solve();

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test10x10DeterminantDouble() {
        double[][] members = new double[][] {
                {1,0,0,-1,-1,1,-1,0,3,-3},
                {2,3,-4,-2,2,3,-2,-4,0,4},
                {-1,-3,3,0,0,-3,4,4,-3,-2},
                {3,-3,4,3,-1,0,-2,-1,-4,1},
                {-5,-5,1,4,0,2,-2,-2,0,4},
                {-5,-5,-4,-4,1,-4,-4,-5,3,2},
                {-1,-5,-4,2,-2,-4,-4,0,0,-5},
                {1,3,-3,0,0,-5,1,-4,1,0},
                {-2,0,-5,4,3,3,-5,1,4,-2},
                {-2,-2,-2,-5,0,4,-3,-3,3,-5},
        };
        Double expected = -39656706d;

        DeterminantLUSolver<Double> instance = new DeterminantLUSolver<>(new Matrix<>(members, new DoubleFactory()));

        Double result = instance.solve();

        assertEquals(expected, result);
    }
}
