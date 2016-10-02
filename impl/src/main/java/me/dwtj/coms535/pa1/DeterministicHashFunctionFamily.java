package me.dwtj.coms535.pa1;

import org.getopt.util.hash.FNV164;

/**
 * An instance of this class represents a family of <em>k</em> 32-bit deterministic hash
 * functions by following the example of Kirsch and Mitzenmacher (2006).
 *
 * This family of 32-bit hash functions is derived from a single 64-bit FNV1 hash. This hash's
 * results are sliced in two to make a pair of 32-bit hash functions: the lower 32-bits are used as
 * one hash function, <em>h1</em>, and the upper 32-bits are used as the other, <em>h2</em>. Then,
 * for each <em>i</em> in our family of <em>k</em> hash functions, we use this pair of hash
 * functions as suggested by Kirsch and Mitzenmacher:
 *
 * <pre>
 * <em>g_i(x) = h1(x) + i*h2(x)</em>.
 * </pre>
 *
 * For performance, this class is designed to minimize object allocation. Its usage follows that of
 * FNV library which we build upon, and that library was in turn followed the design of the
 * public FNV reference implementation.
 *
 * @author dwtj
 *
 * @see <a href="https://dx.doi.org/10.1007/11841036_42">
 *        Kirsch and Mitzenmacher, "Less Hashing, Same Performance: Building a Better Bloom Filter"
 *      </a>
 * @see <a href="http://isthe.com/chongo/tech/comp/fnv/">FNV Hash</a>
 * @see <a href="http://www.getopt.org/utils/build/api/">org.getopt.util.hash</a>
 */
public class DeterministicHashFunctionFamily {

    private static final long MASK = (1L << 32) - 1L;  // 64-Bit Bit Mask with lower 32-bits set.

    private final FNV164 fnv64Hash = new FNV164();
    private long low;
    private long high;

    public void init(String s) {
        fnv64Hash.init(s);
        long orig = fnv64Hash.getHash();
        low = orig & MASK;
        high = orig >> 32;
    }

    public long getHash(long i) {
        return (low + i*high) & MASK;
    }
}
