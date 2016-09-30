package me.dwtj.coms535.pa1;
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
    public void add(String s);

    /**
     * Returns {@code true} iff {@code s} appears in the filter.
     */
    public boolean appears(String s);

    /**
     * Returns the size of the filter, that is, the size of the table used to back the data
     * structure.
     */
    public long filterSize();

    /**
     * Returns the number of elements which have been added to the filter.
     */
    public long dataSize();

    /**
     * Returns the number of hash functions used.
     */
    public long numHashes();
}
