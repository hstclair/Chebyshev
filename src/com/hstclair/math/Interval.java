package com.hstclair.math;

/**
 * @author hstclair
 * @since 8/13/15 6:52 PM
 */
public class Interval {
    public final double a;

    public final double b;

    public final boolean aClosed;

    public final boolean bClosed;

    public Interval(double a) { this(a, true, a, true); }

    public Interval(double a, double b) { this(a, true, b, true); }

    public Interval(double a, boolean aClosed, double b, boolean bClosed) {
        if (a > b) {
            double tmp = a;
            a = b;
            b = tmp;
        }

        this.a = a;
        this.aClosed = aClosed;

        this.b = b;
        this.bClosed = bClosed;
    }

    public boolean contains(double x) {
        if (aClosed && x == a)
            return true;

        if (bClosed && x == b)
            return true;

        return a < x && x < b;
    }
}
