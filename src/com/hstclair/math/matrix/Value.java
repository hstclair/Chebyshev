package com.hstclair.math.matrix;

/**
 * Created by hstclair on 4/22/17.
 */
public interface Value<T> {
    Value<T> add(Value<T> addend);
    Value<T> subtract(Value<T> minuend);
    Value<T> multiply(Value<T> multiplicand);
    Value<T> divide(Value<T> divisor);
    Value<T> negate();
    boolean isZero();
    boolean isOne();
    T value();
}
