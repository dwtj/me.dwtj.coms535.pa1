package me.dwtj.coms535.pa1;

import static java.lang.Math.incrementExact;
import static java.lang.Math.log;
import static java.lang.Math.round;

/**
 * Stores an approximation of a set of strings using a bloom filter with deterministic hash
 * functions.
 */
public class BloomFilterDet implements BloomFilter {

    private final long filterSize;
    private final long numHashes;
    private final LongBitSet table = new LongBitSet();

    private final DeterministicHashFunctionFamily hashes = new DeterministicHashFunctionFamily();

    private long dataSize = 0;

    public BloomFilterDet(int setSize, int bitsPerElement) {
        if (setSize <= 0)
            throw new IllegalArgumentException("Illegal `setSize`: " + setSize);
        if (bitsPerElement <= 0)
            throw new IllegalArgumentException("Illegal `bitsPerElement`: " + bitsPerElement);

        filterSize = setSize * bitsPerElement;
        if (filterSize >= 1L << 32) {
            throw new IllegalArgumentException("Filter size is too large: " + filterSize);
        }

        numHashes = round(bitsPerElement * log(2));
    }

    /** {@inheritDoc} */
    @Override
    public void add(String s) {
        if (s == null)
            throw new IllegalArgumentException("`s` cannot be `null`.");

        s = s.toLowerCase();
        dataSize = incrementExact(dataSize);
        hashes.init(s);
        for (long idx = 0; idx < numHashes; idx++) {
            long tableIdx = hashes.getHash(idx) % filterSize;
            table.set(tableIdx, true);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean appears(String s) {
        if (s == null)
            throw new IllegalArgumentException("`s` cannot be `null`.");

        s = s.toLowerCase();
        hashes.init(s);
        for (long idx = 0; idx < numHashes; idx++) {
            long tableIdx = hashes.getHash(idx) % filterSize;
            if (! table.get(tableIdx)) {
                return false;  // One of the bits was not set, so the item must not be in the set.
            }
        }
        return true;  // All of the bits were set, so the item may be in the set.
    }

    /** {@inheritDoc} */
    @Override
    public long filterSize() {
        return filterSize;
    }

    /** {@inheritDoc} */
    @Override
    public long dataSize() {
        return dataSize;
    }

    /** {@inheritDoc} */
    @Override
    public long numHashes() {
        return numHashes;
    }
}
