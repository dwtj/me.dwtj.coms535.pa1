package me.dwtj.coms535.pa1;

import java.util.Random;

import static java.lang.Math.round;

/**
 * An instance of a universal random hash function. Each hash function instance is of the form
 * <em>(ax + b) % p</em>, where <em>a</em> and <em>b</em> are randomly generated integers. Design
 * details follow the method described by Cormen et. al, 2009, Section 11.3.3. The prime number
 *
 * @author dwtj
 */
public class RandomHashFunction {
    private static final long PRIME = 4294967311L;   // A prime number greater than 2^32
    private static final long BITMASK = (1L << 32) - 1L;  // The lowest 32-bits set to 1.

    private long a, b;

    RandomHashFunction() {
        // Loops are used to guard against illegal values:
        Random rand = new Random();
        do {
            a = round(rand.nextDouble() * PRIME);
        } while (a == 0 || a == PRIME);
        do {
            b = round(rand.nextDouble() * PRIME);
        } while (b == PRIME);
    }

    public long hash(int x) {
        return ((a*x + b) % PRIME) & BITMASK;
    }
}
