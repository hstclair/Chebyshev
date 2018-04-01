package com.hstclair.math.matrix;

import com.hstclair.math.util.DoubleFactory;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestMatrixLUDecomposition {

    @Test
    public void test2X2Double() {

        DoubleFactory doubleFactory = new DoubleFactory();

        Matrix<Double> value = new Matrix<>(new double[][] { { 4, 3 }, { 6, 3 } }, doubleFactory);
        Matrix<Double> expected = new Matrix<>(new double[][] { { 4, 3 }, { 0, -1.5 } }, doubleFactory);

        MatrixLUDecomposition instance = new MatrixLUDecomposition();

        Matrix<Double> result = instance.computeUpper(value);

        assertEquals(expected, result);
    }


    @Test
    public void test3x3Double() {

        DoubleFactory doubleFactory = new DoubleFactory();

        Matrix<Double> value = new Matrix<>(new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} }, doubleFactory);
        Matrix<Double> expected = new Matrix<>(new double[][] { {6, 1, 1}, {0, -6, 1}, {0, 0, 6} }, doubleFactory);

        MatrixLUDecomposition instance = new MatrixLUDecomposition();

        Matrix<Double> result = instance.computeUpper(value);

        assertEquals(expected, result);
    }

}
