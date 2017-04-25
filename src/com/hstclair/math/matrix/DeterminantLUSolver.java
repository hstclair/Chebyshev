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


    // for an nXn matrix M:
    // organize matrix so that initial k rows are zero below diagonal
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
