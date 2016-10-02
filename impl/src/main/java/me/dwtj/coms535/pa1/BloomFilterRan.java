package me.dwtj.coms535.pa1;

import static java.lang.Math.incrementExact;
import static java.lang.Math.log;
import static java.lang.Math.round;

/**
 * Stores an approximation of a set of strings using a bloom filter with random hash functions.
 */
public class BloomFilterRan implements BloomFilter {

    private final long filterSize;
    private final long numHashes;
    private final RandomHashFunction[] hashes;
    private final LongBitSet table = new LongBitSet();

    private long dataSize = 0;

    public BloomFilterRan(int setSize, int bitsPerElement) {
        if (setSize <= 0)
            throw new IllegalArgumentException("Illegal `setSize`: " + setSize);
        if (bitsPerElement <= 0)
            throw new IllegalArgumentException("Illegal `bitsPerElement`: " + bitsPerElement);

        filterSize = setSize * bitsPerElement;
        numHashes = round(bitsPerElement * log(2));
        hashes = new RandomHashFunction[(int) numHashes];  // Cast okay, because log(2) < 1.
        for (int idx = 0; idx < numHashes; idx++) {
            hashes[idx] = new RandomHashFunction();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void add(String s) {
        if (s == null)
            throw new IllegalArgumentException("`s` cannot be `null`.");

        s = s.toLowerCase();
        dataSize = incrementExact(dataSize);
        for (RandomHashFunction fn : hashes) {
            table.set(tableIndex(fn, s), true);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean appears(String s) {
        if (s == null)
            throw new IllegalArgumentException("`s` cannot be `null`.");

        s = s.toLowerCase();
        for (RandomHashFunction fn : hashes) {
            if (! table.get(tableIndex(fn, s))) {
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

    private long tableIndex(RandomHashFunction fn, String s) {
        return fn.hash(s.hashCode()) % filterSize;
    }
}
