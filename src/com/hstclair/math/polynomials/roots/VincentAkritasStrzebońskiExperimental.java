package com.hstclair.math.polynomials.roots;

import com.hstclair.math.Interval;
import com.hstclair.math.RealMobiusTransformation;
import com.hstclair.math.polynomials.Polynomial;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hstclair
 * @since 8/10/15 7:20 PM
 */
public class VincentAkritasStrzebońskiExperimental {

    List<Interval> performVASIteration(VASOperation operation) {
        List<Interval> results = new LinkedList<>();
        LinkedList<VASOperation> operations = new LinkedList<>();

        operations.add(operation);

        while (! operations.isEmpty()) {
            VASOperation nextOperation = operations.removeFirst();

            List<VASOperation> output = nextOperation.evaluate();

            for (VASOperation result : output) {
                if (result.complete())
                    results.addAll(result.getResults());
                else
                    operations.addLast(result);
            }
        }

        return results;
    }

    public List<Interval> findRootIntervals(Polynomial polynomial) {
        // compute s = sgc(f)
        int signs = polynomial.signChanges();

        // if s == 0 return empty list
        if (signs == 0) return Collections.EMPTY_LIST;

        // if s == 1 return {(0, inf)}
        if (signs == 1) {
            Interval interval = new Interval(0, Double.POSITIVE_INFINITY);

            return Collections.singletonList(interval);
        }

        // Put interval data {1, 0, 0, 1, f, s} on intervalstack
        return performVASIteration(new VASComputationExperimental(polynomial, RealMobiusTransformation.IDENTITY));
    }
}




