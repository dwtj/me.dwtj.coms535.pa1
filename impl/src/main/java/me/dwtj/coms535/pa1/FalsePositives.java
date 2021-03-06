package me.dwtj.coms535.pa1;

import me.dwtj.coms535.pa1.BloomFilter.Maker;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import static java.lang.Math.ceil;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.String.format;
import static java.lang.System.out;
import static java.time.Instant.now;
import static java.util.Arrays.sort;

/**
 * An experiment to empirically estimate the false positive probability rate of two bloom filter
 * implementations.
 *
 * <p>We study two independent variables: the size of the set stored in the bloom filter,
 * <em>n</em>, and the number of bits per element, <em>b</em>. We vary these values to see their
 * effects on the estimated false probability rate.
 *
 * <p>We study all combinations of {@link #BITS_PER_ELEMENT_VALUES} and {@link #SET_SIZE_VALUES}.
 * Let our choice from {@link #BITS_PER_ELEMENT_VALUES} be denoted by <em>b</em>, and let our choice
 * of {@link #SET_SIZE_VALUES} be denoted by <em>n</em>.
 *
 * <p>For each of these combinations of <em>n</em> and <em>b</em>, we perform {@link #NUM_TRIALS}
 * trials. In each trial, the set is populated with randomly generated elements. Each random element
 * is generated by {@link #randomString}. (<strong>Warning:</strong> This method of generating
 * random strings is not uniformly random across all strings.)
 *
 * <p>Once the bloom filter is initialized with <em>n</em> elements, we perform a certain number of
 * queries with values which were <em>not</em> added to the bloom filter. By counting the number of
 * queries which are accepted, we are counting the number of false positives, and from this we can
 * then compute the observed false positive rate in each trial, <em>p*</em>.
 *
 * <p>We follow the example of <em>Kirsch and Mitzenmacher (2006)</em> to select the number of
 * queries to be performed in each trial. In their formulation, they perform {@code ceil(10/p)}
 * queries, where <em>p</em> is the analytically expected false positive rate. As stated in the
 * assignment document, {@code pa1.pdf}, <em>p</em> is a function of <em>b</em>, the number of bits
 * per element. In particular, <em>p = (0.618)^b</em>. {@link #numQueries} computes this function.
 *
 * <p>After all {@link #NUM_TRIALS} trials are performed, we take the mean of these observed false
 * positive rates to produce our estimated false positive rate, <em>p*</em>, for some combination
 * of <em>n</em> and <em>b</em>. This result is printed to {@code STDOUT}.
 *
 * In this way, we compute and print out our measured false positive rate for all combinations of
 * <em>n</em> and <em>b</em>.
 *
 * @see <a href="https://dx.doi.org/10.1007/11841036_42">
 *        Kirsch and Mitzenmacher, "Less Hashing, Same Performance: Building a Better Bloom Filter"
 *      </a>
 *
 * @author dwtj
 */
public class FalsePositives {

    public static void main(String[] args) {
        Maker[] makers = {
            BloomFilterDet::new,
            BloomFilterRan::new
        };

        for (Maker maker :  makers) {
            for (int b : BITS_PER_ELEMENT_VALUES) {
                for (int n : SET_SIZE_VALUES) {
                    double p = expectedRate.apply(b);
                    int q = numQueries.apply(b);

                    Instant start = now();
                    double pActual = falsePositiveRate(NUM_TRIALS, n, b, q, maker);
                    Instant end = now();
                    Duration duration = Duration.between(start, end);

                    String fmt = "n = %d, b = %d, p = %.8f, p* = %.8f, p* / p = %.8f, duration = %ds";
                    out.println(format(fmt, n, b, p, pActual, pActual/p, duration.getSeconds()));
                }
            }
            out.println();
        }
    }

    public static final Function<Integer, Double> expectedRate =
            (b) -> pow(0.618, b);

    public static final Function<Integer,Integer> numQueries =
            (b) -> (int) round(ceil(10.0 / expectedRate.apply(b)));

    public static final int NUM_TRIALS = 10000;

    public static final int[] BITS_PER_ELEMENT_VALUES = { 4, 8, 12, 16 };

    public static final int[] SET_SIZE_VALUES = {
        5000, 10000, 15000, 20000, 25000, 30000, 35000, 40000, 45000, 50000
    };

    /**
     * Measures the false positive rate observed on a number of simulated trials.
     *
     * @param trials
     *          The number of trials to be performed.
     * @param n
     *          The size of the random set with which to populate a bloom filter in a trial.
     * @param b
     *          The bits per element factor used for each bloom filter.
     * @param queries
     *          The number of queries to perform on the bloom filters in a trial.
     */
    public static double falsePositiveRate(int trials, int n, int b, int queries, Maker maker) {
        if (trials <= 0 || n <= 0 || b <= 0 || queries <= 0) {
            String msg = "trials = {0}, n = {1}, b = {2}, queries = {3}";
            throw new IllegalArgumentException(MessageFormat.format(msg, trials, n, b, queries));
        }

        double acc = 0;
        for (int idx = 0; idx < trials; idx++) {
            acc += falsePositiveRate(n, b, queries, maker);
        }
        return acc / trials;
    }

    /**
     * This method is similar to {@link #falsePositiveRate(int, int, int, int, Maker)},
     * except it just runs a single trial.
     */
    private static double falsePositiveRate(int n, int b, int queries, Maker maker) {
        BloomFilter filter = maker.apply(n, b);
        String[] set = addRandom(filter, n);

        int falsePositivesCount = 0;
        for (int idx = 0; idx < queries; idx++) {
            String s;
            int loc;
            do {
                s = randomString();
                loc = Arrays.binarySearch(set, s);
            } while (0 <= loc && loc < set.length);  // Loop until an element not in `set` is found.
            if (filter.appears(s)) {
                falsePositivesCount++;
            }
        }

        // The observed false positive rate is the number of observed false positives over the
        // number of query attempts.
        return ((double) falsePositivesCount) / queries;
    }

    private static final Random random = new Random();

    private static String randomString() {
        float f = random.nextFloat();
        return Float.toString(f);
    }

    /**
     * Add <em>n</em> randomly generated strings to the given bloom filter.
     *
     * @param filter
     *          The filter to which strings are added.
     * @param n
     *          The number of random strings to add.
     *
     * @return
     *      A <em>sorted</em> array containing the randomly-generated strings which were added.
     */
    private static String[] addRandom(BloomFilter filter, int n) {
        String[] added = new String[n];
        for (int idx = 0; idx < n; idx++) {
            String s = randomString();
            added[idx] = s;
            filter.add(s);
        }
        sort(added);
        return added;
    }
}