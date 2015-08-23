package com.hstclair.math;

/**
 * @author hstclair
 * @since 8/10/15 8:33 PM
 */
public class DescartesRuleOfSigns {

    public DescartesRuleOfSigns() {}

    public int signChanges(double[] coefficients) {
        int count = -1;
        double lastSign = 0;

        for (double coefficient : coefficients) {
            if ((coefficient == 0) || (lastSign < 0 && coefficient < 0) || (lastSign > 0 && coefficient > 0))
                continue;

            count++;

            lastSign = coefficient;
        }

        return Math.max(count, 0);
    }
}
