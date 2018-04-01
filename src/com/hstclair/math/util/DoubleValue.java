package com.hstclair.math.util;

import com.hstclair.math.matrix.Value;

/**
 * Created by hstclair on 4/22/17.
 */
public class DoubleValue implements Value<Double> {

    final Double value;

    public DoubleValue(Double value) {
        this.value = value;
    }

    @Override
    public Value<Double> multiply(Value<Double> multiplicand) {
        return new DoubleValue(value * multiplicand.value());
    }

    @Override
    public Value<Double> divide(Value<Double> divisor) {
        return new DoubleValue(value / divisor.value());
    }

    @Override
    public Value<Double> add(Value<Double> addend) {
        return new DoubleValue(value + addend.value());
    }

    @Override
    public Value<Double> subtract(Value<Double> minuend) {
        return new DoubleValue(value - minuend.value());
    }

    @Override
    public Value<Double> negate() {
        return new DoubleValue(-value);
    }

    @Override
    public Double value() {
        return value;
    }

    @Override
    public boolean isZero() { return value == 0; }

    @Override
    public boolean isOne() { return value == 1; }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (obj == this)
            return true;

        if (! (obj instanceof DoubleValue))
            return false;

        DoubleValue that = (DoubleValue) obj;

        return value.equals(that.value());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
