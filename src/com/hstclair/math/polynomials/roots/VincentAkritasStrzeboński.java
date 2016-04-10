package com.hstclair.math.polynomials.roots;

import com.hstclair.math.Interval;
import com.hstclair.math.RealMobiusTransformation;
import com.hstclair.math.polynomials.Polynomial;

import java.util.*;
import java.util.function.Function;

/**
 * @author hstclair
 * @since 8/10/15 7:20 PM
 */
public class VincentAkritasStrzeboński {

    private Function<Polynomial, VASOperation> vasOperationBuilder;

    /** Construct standard VincentAkritasStrzeboński instance */
    public VincentAkritasStrzeboński() {
        this((Polynomial polynomial) -> new VASComputation(polynomial, RealMobiusTransformation.IDENTITY));
    }

    /**
     * Construct VincentAkritasStrzeboński instance with custom implementation of VASComputation
     * @param vasOperationBuilder function to construct the required VASComputation implementation
     */
    public VincentAkritasStrzeboński(Function<Polynomial, VASOperation> vasOperationBuilder) {
        this.vasOperationBuilder = vasOperationBuilder;
    }

    List<Interval> performVASIteration(VASOperation operation) {
        Set<Interval> results = new HashSet<>();
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

        return new LinkedList<>(results);
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
        return performVASIteration(vasOperationBuilder.apply(polynomial));
    }
}




