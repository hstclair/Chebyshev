package com.hstclair.math.matrix;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hstclair on 4/21/17.
 */
public class DeterminantSolver<T> {
    class Task {
        final Value<T> coefficient;

        final Matrix<T> matrix;

        Task(Value<T> coefficient, Matrix<T> matrix) {
            this.coefficient = coefficient;
            this.matrix = matrix;
        }

        boolean canSolve() {
            return matrix.order == 2;
        }

        Value<T> evaluate() {
            return coefficient.multiply(matrix.member(0,0).multiply(matrix.member(1, 1)).subtract(matrix.member(0, 1).multiply(matrix.member(1, 0))));
        }

        List<Task> createTasks() {
            List<Task> result = new ArrayList<>();

            if (canSolve()) {   // handle the edge case that occurs when initial matrix is 2x2
                result.add(this);
                return result;
            }

            boolean negate = true;

            for (int col = 0; col < matrix.order; col++) {
                Value newCoefficient = matrix.member(0, col);

                negate = !negate;

                if (newCoefficient.equals(matrix.factory.valueOfZero()))
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

    public DeterminantSolver(Matrix<T> matrix) {
        tasks.add(new Task(matrix.factory.valueOfOne(), matrix));
    }

    public Value<T> solve() {
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

        Value<T> accumulator = null;

        while (! results.isEmpty()) {
            Task result = results.remove();

            Value<T> evaluation = result.evaluate();

            if (accumulator == null)
                accumulator = evaluation;
            else
                accumulator = accumulator.add(evaluation);
        }

        return accumulator;
    }

}
