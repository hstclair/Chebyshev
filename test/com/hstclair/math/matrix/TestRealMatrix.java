package com.hstclair.math.matrix;

import org.apfloat.Apfloat;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by hstclair on 4/17/17.
 */
public class TestRealMatrix {

    @Test
    public void test3x3Determinant() {
        double expected = -306;

        double[][] members = new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} };

        RealMatrix instance = new RealMatrix(members);

        double result = instance.determinant();

        assertEquals(expected, result);
    }

    @Test
    public void test4x4Determinant() {
        double expected = -418;

        double[][] members = new double[][] { {3, 2, -1, 4}, {2, 1, 5, 7}, {0, 5, 2, -6}, {-1, 2, 1, 0} };

        RealMatrix instance = new RealMatrix(members);

        double result = instance.determinant();

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

        double expected = -61089854;

        RealMatrix instance = new RealMatrix(members);

        double result = instance.determinant();

        assertEquals(expected, result);
    }


    @Test
    public void test10X10Determinant() {
        double[][] members = new double[][] {
                { 21, 45, 5, 17, 60, 47, 52, 87, 14, 36 },
                { 81, 20, 84, 48, 66, 10, 86, 74, 5, 0 },
                { 72, 99, 47, 96, 20, 64, 94, 22, 5, 91 },
                { 0, 58, 96, 89, 27, 2, 91, 61, 4, 63 },
                { 24, 25, 53, 83, 5, 45, 18, 42, 20, 7 },
                { 49, 58, 99, 75, 96, 99, 10, 20, 53, 28 },
                { 21, 6, 52, 97, 47, 80, 5, 50, 23, 40 },
                { 29, 44, 90, 96, 36, 38, 44, 16, 26, 72 },
                { 77, 3, 17, 60, 60, 14, 60, 15, 71, 16 },
                { 61, 82, 45, 16, 29, 39, 65, 75, 98, 22 }
        };
        double expected = 1859682092504620000l;

        RealMatrix instance = new RealMatrix(members);

        double result = instance.determinant();

        assertEquals(expected, result);
    }

}
