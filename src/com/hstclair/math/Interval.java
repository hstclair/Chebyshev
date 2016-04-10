package com.hstclair.math;

import java.util.DoubleSummaryStatistics;

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

    public boolean isExactValue() {
        return (aClosed && bClosed && a == b);
    }

    public double getExactValue() {
        if (! isExactValue())
            throw new IllegalStateException("Interval is not an exact value");

        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (getClass() != o.getClass() && Double.class != o.getClass())) return false;

        if (o.getClass() == getClass()) {
            Interval interval = (Interval) o;

            if (Double.compare(interval.a, a) != 0) return false;
            if (Double.compare(interval.b, b) != 0) return false;
            if (aClosed != interval.aClosed) return false;
            return bClosed == interval.bClosed;
        }

        if (! isExactValue()) return false;

        return a == (double) o;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(a);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (aClosed ? 1 : 0);
        result = 31 * result + (bClosed ? 1 : 0);
        return result;
    }

    String inequality(boolean closed) {
        if (closed)
            return "<=";

        return "<";
    }

    @Override
    public String toString() {
        if (isExactValue())
            return String.format("exactly %f", a);

        return String.format("interval %f %s X %s %f", a, inequality(aClosed), inequality(bClosed), b );
    }
}
