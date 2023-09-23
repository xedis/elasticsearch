/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.compute.data;

import java.util.Arrays;

/**
 * Block build of LongBlocks.
 * This class is generated. Do not edit it.
 */
final class LongVectorBuilder extends AbstractVectorBuilder implements LongVector.Builder {

    private long[] values;

    LongVectorBuilder(int estimatedSize, BlockFactory blockFactory) {
        super(blockFactory);
        int initialSize = Math.max(estimatedSize, 2);
        adjustBreaker(initialSize);
        values = new long[Math.max(estimatedSize, 2)];
    }

    @Override
    public LongVectorBuilder appendLong(long value) {
        ensureCapacity();
        values[valueCount] = value;
        valueCount++;
        return this;
    }

    @Override
    protected int elementSize() {
        return Long.BYTES;
    }

    @Override
    protected int valuesLength() {
        return values.length;
    }

    @Override
    protected void growValuesArray(int newSize) {
        values = Arrays.copyOf(values, newSize);
    }

    @Override
    public LongVector build() {
        LongVector vector;
        if (valueCount == 1) {
            vector = new ConstantLongVector(values[0], 1, blockFactory);
        } else {
            if (values.length - valueCount > 1024 || valueCount < (values.length / 2)) {
                values = Arrays.copyOf(values, valueCount);
            }
            vector = new LongArrayVector(values, valueCount, blockFactory);
        }
        // update the breaker with the actual bytes used.
        blockFactory.adjustBreaker(vector.ramBytesUsed() - estimatedBytes, true);
        return vector;
    }
}
