package com.hstclair.math.matrix;

/**
 * Created by hstclair on 4/22/17.
 */
public class DeterminantLUSolver<T> {

    final Value<T> coefficient;

    final Matrix<T> matrix;

    public DeterminantLUSolver(Matrix<T> matrix) {
        this.matrix = matrix;
    }

    enum RowSolutionState {
        NoSolution,     // matrix has no solution
        Solveable,      // row may be solved
        Exchanged       // row may be solved but rows were transposed
    }

    // X X X
    // 0 X X
    // 0 0 X

    int firstSolveableRow(Value<T>[][] members, int column) {
        for (int row = column; row < members.length; row++) {
            if (! members[row][column].isZero()) {
                return row;
            }
        }

        return -1;
    }

    Value<T>[][] switchRows(Value<T>[][] members, int rowA, int rowB) {
        Value<T>[] temp = members[rowA];
        members[rowA] = members[rowB];
        members[rowB] = temp;

        return members;
    }

    RowSolutionState prepColumn(Value<T>[][] members, int column) {
        int row = firstSolveableRow(members, column);

        if (row < column) {
            return RowSolutionState.NoSolution;   // column cannot be solved (therefore, matrix cannot be solved)
        }

        if (row == column) {
            return RowSolutionState.Solveable;    // matrix is ready for column to be solved
        }

        switchRows(members, column, row);
        return RowSolutionState.Exchanged;        // matrix is ready for column to be solved but rows were exchanged
    }

    void solveColumn(Value<T>[][] members, int solveColumn) {
        for (int row = solveColumn + 1; row < members.length; row++) {
            if (members[row][solveColumn].isZero())
                continue;

            for (int column = solveColumn; column < members[row].length; column++) {
                if (members[row][column].isZero())
                    members[row][column] = members[row][solveColumn].multiply(members[solveColumn][column]);
                else
                    members[row][column] = members[row][column].multiply(members[solveColumn][solveColumn]).subtract(members[row][solveColumn].multiply(members[solveColumn][column]));
            }
        }
    }

    T solveMatrix(Value<T>[][] members, int order) {
        for (int column = 0; column < order; column++) {
            solveColumn(members, column);
        }

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
