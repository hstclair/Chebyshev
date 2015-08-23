package com.hstclair.math.polynomials.roots;

import com.hstclair.math.Interval;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hstclair
 * @since 8/22/15 4:00 PM
 */
public class VASResult implements VASOperation {
    public static final VASResult NOTHING = new VASResult();

    final List<Interval> results;

    VASResult() { results = new LinkedList<>(); }

    public VASResult(List<Interval> intervals) {
        this.results = intervals;
    }

    @Override
    public boolean complete() {
        return true;
    }

    @Override
    public List<VASOperation> evaluate() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Interval> getResults() {
        return results;
    }
}
