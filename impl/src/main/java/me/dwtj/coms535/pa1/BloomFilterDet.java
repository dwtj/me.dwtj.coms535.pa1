package me.dwtj.coms535.pa1;

import org.getopt.util.hash.FNV164;

/**
 * Stores an approximation of a set of strings using a bloom filter with deterministic hash
 * functions.
 */
public class BloomFilterDet implements BloomFilter {

    private final int setSize, bitsPerElement;
    private final FNV164 hash = new FNV164();

    public BloomFilterDet(int setSize, int bitsPerElement) {
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
