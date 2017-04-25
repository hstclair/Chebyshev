package com.hstclair.math.matrix;

import org.apfloat.Apfloat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hstclair on 4/21/17.
 */
public class DeterminantSolverApfloat {
    class Task {
        final Apfloat coefficient;

        final RealBigMatrix matrix;

        public Task(Apfloat coefficient, RealBigMatrix matrix) {
            this.coefficient = coefficient;
            this.matrix = matrix;
        }

        public boolean canSolve() {
            return matrix.order == 2;
        }

        public Apfloat evaluate() {
            return coefficient.multiply(matrix.members[0][0].multiply(matrix.members[1][1]).subtract(matrix.members[0][1].multiply(matrix.members[1][0])));
        }

        List<Task> createTasks() {
            List<Task> result = new ArrayList<>();

            if (canSolve()) {   // handle the edge case that occurs when initial matrix is 2x2
                result.add(this);
                return result;
            }

            boolean negate = true;

            for (int col = 0; col < matrix.order; col++) {
                Apfloat newCoefficient = matrix.members[0][col];

                negate = !negate;

                if (newCoefficient.equals(Apfloat.ZERO))
                    continue;

                if (negate)
                    newCoefficient = newCoefficient.negate();

                result.add(new Task(newCoefficient.multiply(coefficient), matrix.minor(0, col)));
            }

            return result;
        }
    }

    final LinkedList<Task> tasks = new LinkedList<>();
    final LinkedList<Task> results = new LinkedList<>();

    public DeterminantSolverApfloat(RealBigMatrix matrix) {
        tasks.add(new Task(Apfloat.ONE, matrix));
    }

    public Apfloat solve() {
        while (! tasks.isEmpty()) {
            Task task = tasks.removeLast();

            List<Task> newTasks = task.createTasks();

            for (Task newTask : newTasks) {
                if (newTask.canSolve()) {
                    results.add(newTask);
                } else {
                    tasks.addFirst(newTask);
                }
            }
        }

        Apfloat accumulator = Apfloat.ZERO;

        while (! results.isEmpty()) {
            Task result = results.remove();

            Apfloat evaluation = result.evaluate();

            accumulator = accumulator.add(evaluation);
        }

        return accumulator;
    }

}
