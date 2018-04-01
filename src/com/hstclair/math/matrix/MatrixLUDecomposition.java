package com.hstclair.math.matrix;

import com.hstclair.math.util.ValueFactory;

public class MatrixLUDecomposition {

    public MatrixLUDecomposition() {
    }

    enum RowSolutionState {
        NoSolution,     // matrix has no solution
        Solvable,      // row may be solved
        Exchanged       // row may be solved but rows were transposed
    }

    // X X X
    // 0 X X
    // 0 0 X

    <T> int firstSolveableRow(Value<T>[][] members, int column) {
        for (int row = column; row < members.length; row++) {
            if (! members[row][column].isZero()) {
                return row;
            }
        }

        return -1;
    }

    <T> Value<T>[][] switchRows(Value<T>[][] members, int rowA, int rowB) {
        Value<T>[] temp = members[rowA];
        members[rowA] = members[rowB];
        members[rowB] = temp;

        return members;
    }

    <T> DeterminantLUSolver.RowSolutionState prepColumn(Value<T>[][] members, int column) {
        int row = firstSolveableRow(members, column);

        if (row < column) {
            return DeterminantLUSolver.RowSolutionState.NoSolution;   // column cannot be solved (therefore, matrix cannot be solved)
        }

        if (row == column) {
            return DeterminantLUSolver.RowSolutionState.Solvable;    // matrix is ready for column to be solved
        }

        switchRows(members, column, row);
        return DeterminantLUSolver.RowSolutionState.Exchanged;        // matrix is ready for column to be solved but rows were exchanged
    }

    <T> void solveRow(Value<T>[] referenceRow, Value<T>[] row, int solveColumn, Value<T> divisor, Value<T> ZERO, Value<T> ONE) {

        for (int column = solveColumn+1; column < row.length; column++) {

            if (! row[column].isZero())
                row[column] = row[column].multiply(referenceRow[solveColumn]);

            row[column] = row[column].subtract(row[solveColumn].multiply(referenceRow[column]));

            if (! (row[column].isZero() || divisor == ONE))
                row[column] = row[column].divide(divisor);
        }

        row[solveColumn] = ZERO;
    }

    <T> Value<T> solveColumn(Value<T>[][] members, int solveColumn, Value<T> divisor, Value<T> ZERO, Value<T> ONE) {

        for (int row = solveColumn + 1; row < members.length; row++) {
            solveRow(members[solveColumn], members[row], solveColumn, divisor, ZERO, ONE);
        }

        return members[solveColumn][solveColumn];
    }

    <T> Matrix<T> computeUpper(Value<T>[][] members, ValueFactory<T> valueFactory) {

        boolean negate = false;

        Value<T> divisor = valueFactory.valueOfOne();

        Value<T> prevDivisor = valueFactory.valueOfOne();

        int order = members.length;

        for (int column = 0; column < order; column++) {
            DeterminantLUSolver.RowSolutionState state = prepColumn(members, column);

            if (state == DeterminantLUSolver.RowSolutionState.NoSolution)
                return null;

            if (state == DeterminantLUSolver.RowSolutionState.Exchanged)
                negate = ! negate;

            prevDivisor = divisor;

            divisor = solveColumn(members, column, divisor, valueFactory.valueOfZero(), valueFactory.valueOfOne());
        }

        members[order - 1][order - 1] = members[order - 1][order - 1].divide(prevDivisor);

        if (negate)
            members[order-1][order-1] = members[order-1][order-1].negate();

        return new Matrix<>(members, valueFactory);
    }

    public <T> Matrix<T> computeUpper(Matrix<T> matrix) {

        Value<T>[][] members = matrix.cloneMembers();    // get member array

        return computeUpper(members, matrix.factory);
    }


    // for an nXn matrix M:
    // for each column i of matrix
    //    1. Make sure that the value of M[i][i] is non-zero (swap rows if needed)
    //    2. For each subsequent row j for which M[j][i] is non-zero:
    //       1. For each column k (including column i):
    //          1. Set M[j][k] equal to M[j][k]*M[i][i]-M[j][i]*M[i][k]
    //
    // (i.e. first row has first column non-zero, second row has first column zero and second column non-zero,
    //   third row has first and second columns zero and third column non-zero, etc.)
    // Add the number of rows exchanged during this process to the total rows exchanged
    // Reduce all subsequent rows (using initial rows) so that initial k column(s) are zero for each row below row k
    // Repeat sort

    // when reducing any row R in the nXn matrix M by a row k T:
    // 1. set the first k+1 columns of result set to 0
    // 2. for each remaining column j (in k through n) of the reduced row R:
    //    set result column j to (R[j]*T[k] - R[k]*T[j])/M[k-1][k-1]

    // when all rows have been exhausted:
    // If the number of rows exchanged during all sorting steps is odd, negate the value in M[n][n]
    // The value in M[n][n] is the determinant of the matrix

}
