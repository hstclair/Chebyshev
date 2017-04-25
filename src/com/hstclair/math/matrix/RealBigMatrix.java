package com.hstclair.math.matrix;

import com.hstclair.math.vector.RealVector;
import org.apfloat.Apfloat;

/**
 * Created by hstclair on 4/17/17.
 */
public class RealBigMatrix {

    final Apfloat[][] members;

    final int rows;
    final int columns;
    final long precision;
    final int order;

    public RealBigMatrix(double[][] members) {
        this(members, 1000);
    }

    public RealBigMatrix(Apfloat[][] members) {
        this.members = members;
        rows = members.length;
        columns = columns(members);
        order = Math.min(rows, columns);
        precision = precision(members);
    }

    public RealBigMatrix(double[][] members, long precision) {
        int columnCount = 0;
        this.precision = precision;
        this.members = new Apfloat[members.length][members[0].length];
        for (int rowIndex=0; rowIndex<members.length; rowIndex++) {
            for (int colIndex = 0; colIndex<members[rowIndex].length; colIndex++) {
                this.members[rowIndex][colIndex] = new Apfloat(members[rowIndex][colIndex], precision);
            }
            columnCount = Math.max(columnCount, members[rowIndex].length);
        }
        rows = members.length;
        columns = columnCount;
        order = Math.min(rows, columns);
    }

    private int columns(Apfloat[][] members) {
        int columns = 0;

        for (int row=0; row < members.length; row++) {
            columns = Math.max(columns, members[row].length);
        }

        return columns;
    }

    private long precision(Apfloat[][] members) {
        long precision = 0;

        for (Apfloat[] row: members) {
            for (Apfloat member : row) {
                precision = Math.max(precision, member.precision());
            }
        }

        return precision;
    }

    private Apfloat[][] clone(Apfloat[][] original) {
        Apfloat[][] clone=new Apfloat[original.length][];

        int columns = columns(original);

        for (int row=0; row < original.length; row++) {
            clone[row] = new Apfloat[columns];
            System.arraycopy(original[row], 0,clone[row],0, columns);
        }

        return clone;
    }

    private Apfloat subtract(Apfloat[] subtrahend, Apfloat[] minuend, int column) {
        Apfloat subtrahendCoefficient = minuend[column];
        Apfloat minuendCoefficient = subtrahend[column];

        for (int index = 0; index < subtrahend.length; index++) {
            subtrahend[index] = subtrahend[index].multiply(subtrahendCoefficient).subtract(minuend[index].multiply(minuendCoefficient));
        }

        return subtrahendCoefficient;
    }

    public Apfloat determinant() {
        Apfloat[][] umembers=clone(this.members);
        Apfloat determinant = Apfloat.ONE;
        Apfloat coefficient = Apfloat.ONE;

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

    public RealBigMatrix minor(int mrow, int mcolumn) {
        Apfloat[][] members = new Apfloat[rows-1][columns-1];

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

        return new RealBigMatrix(members);
    }
}
