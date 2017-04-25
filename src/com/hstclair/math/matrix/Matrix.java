package com.hstclair.math.matrix;


import com.hstclair.math.util.ValueFactory;

import java.lang.reflect.Array;

/**
 * Created by hstclair on 4/22/17.
 */
public class Matrix<T> {

    private final Value<T>[][] members;

    final int rows;
    final int columns;
    final int order;
    final ValueFactory<T> factory;

    public Matrix(Value<T>[][] members, ValueFactory<T> factory) {
        this.factory = factory;
        this.members = members;
        rows = members.length;
        columns = columns(members);
        order = Math.min(rows, columns);
    }

    public Matrix(double[][] members, ValueFactory<T> factory) {
        int columnCount = 0;
        this.factory = factory;
        this.members = factory.matrixArray(members.length, members[0].length);
        for (int rowIndex=0; rowIndex<members.length; rowIndex++) {
            for (int colIndex = 0; colIndex<members[rowIndex].length; colIndex++) {
                this.members[rowIndex][colIndex] = factory.valueOf(members[rowIndex][colIndex]);
            }
            columnCount = Math.max(columnCount, members[rowIndex].length);
        }
        rows = members.length;
        columns = columnCount;
        order = Math.min(rows, columns);
    }

    private int columns(Value<T>[][] members) {
        int columns = 0;

        for (Value<T>[] row : members) {
            columns = Math.max(columns, row.length);
        }

        return columns;
    }

    private Value<T>[][] clone(Value<T>[][] original) {
        Value<T>[][] clone= factory.matrixArray(original.length, 0);

        int columns = columns(original);

        for (int row=0; row < original.length; row++) {
            clone[row] = factory.vectorArray(columns);
            System.arraycopy(original[row], 0,clone[row],0, columns);
        }

        return clone;
    }

    private Value<T> subtract(Value<T>[] subtrahend, Value<T>[] minuend, int column) {
        Value<T> subtrahendCoefficient = minuend[column];
        Value<T> minuendCoefficient = subtrahend[column];

        for (int index = 0; index < subtrahend.length; index++) {
            subtrahend[index] = subtrahend[index].multiply(subtrahendCoefficient).subtract(minuend[index].multiply(minuendCoefficient));
        }

        return subtrahendCoefficient;
    }

    public Value<T> determinant() {
        Value<T>[][] umembers=clone(this.members);
        Value<T> determinant = factory.valueOfOne();
        Value<T> coefficient = factory.valueOfOne();

        for (int row=0; row < this.rows; row++) {
            for (int col=0; col< this.columns; col++) {
                if (col == row)
                    break;

                coefficient = coefficient.multiply(subtract(umembers[row], umembers[col], col));
            }

            determinant = determinant.multiply(umembers[row][row]);
        }

        return determinant.divide(coefficient);
    }

    public Value<T> member(int row, int column) {
        return members[row][column];
    }

    public Matrix<T> minor(int mrow, int mcolumn) {
        Value<T>[][] members = factory.matrixArray(rows-1, columns-1);

        int row = 0;

        for (int srcRow = 0; srcRow < rows; srcRow++) {

            if (srcRow == mrow)
                continue;

            int col = 0;

            for (int srcCol = 0; srcCol < columns; srcCol++) {

                if (srcCol == mcolumn)
                    continue;

                members[row][col] = this.members[srcRow][srcCol];

                col++;
            }

            row++;
        }

        return new Matrix<T>(members, factory);
    }

}
