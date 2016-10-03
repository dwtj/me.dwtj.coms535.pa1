package me.dwtj.coms535.pa1;

import java.util.function.BiFunction;

/**
 * Stores an approximation of a set of strings using a bloom filter.
 *
 * The set is designed to be case-insensitive; for example, it does not meaningfully distinguish
 * between "Galaxy" and "galaxy".
 */
public interface BloomFilter {

    /**
     * Adds the string {@code s} to the filter.
     */
    void add(String s);

    /**
     * Returns {@code true} iff {@code s} appears in the filter.
     */
    boolean appears(String s);

    /**
     * Returns the size of the filter, that is, the size of the table used to back the data
     * structure.
     */
    long filterSize();

    /**
     * Returns the number of elements which have been added to the filter.
     */
    long dataSize();

    /**
     * Returns the number of hash functions used.
     */
    long numHashes();

    /**
     * A factory function which takes the set size and the number of number of bits per element
     * and creates a bloom filter.
     */
    interface Maker extends BiFunction<Integer, Integer, BloomFilter> { /* Nothing needed here. */ }
}
