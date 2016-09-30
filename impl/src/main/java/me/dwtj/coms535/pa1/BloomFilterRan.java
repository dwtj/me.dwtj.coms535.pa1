package me.dwtj.coms535.pa1;

/**
 * Stores an approximation of a set of strings using a bloom filter with random hash functions.
 */
public class BloomFilterRan implements BloomFilter {

    private final int setSize, bitsPerElement;

    public BloomFilterRan(int setSize, int bitsPerElement) {
        this.setSize = setSize;
        this.bitsPerElement = bitsPerElement;
    }

    /** {@inheritDoc} */
    @Override
    public void add(String s) {
        throw new UnsupportedOperationException("TODO");
    }

    /** {@inheritDoc} */
    @Override
    public boolean appears(String s) {
        throw new UnsupportedOperationException("TODO");
    }

    /** {@inheritDoc} */
    @Override
    public long filterSize() {
        throw new UnsupportedOperationException("TODO");
    }

    /** {@inheritDoc} */
    @Override
    public long dataSize() {
        throw new UnsupportedOperationException("TODO");
    }

    /** {@inheritDoc} */
    @Override
    public long numHashes() {
        throw new UnsupportedOperationException("TODO");
    }
}
