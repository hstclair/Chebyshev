package com.hstclair.math.polynomials.roots;

import com.hstclair.math.Interval;

import java.util.List;

/**
 * @author hstclair
 * @since 8/23/15 4:03 PM
 */
public interface VASOperation {

    boolean complete();

    List<VASOperation> evaluate();

    List<Interval> getResults();
}
