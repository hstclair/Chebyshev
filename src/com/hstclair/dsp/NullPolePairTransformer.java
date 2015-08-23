package com.hstclair.dsp;

/**
 * @author hstclair
 * @since 8/10/15 6:37 PM
 */
public class NullPolePairTransformer implements PolePairTransformer {

    @Override
    public PolePair transform(PolePair polePair, int poles) {
        return polePair;
    }
}
