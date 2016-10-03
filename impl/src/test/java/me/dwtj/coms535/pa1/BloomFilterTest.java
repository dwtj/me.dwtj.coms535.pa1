package me.dwtj.coms535.pa1;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * The tests based on {@link #randomSetTest} may fail on occasion without there being any bugs. I
 * haven't done any analysis yet to see whether the magic numbers are actually appropriate and
 * whether test failures may actually happen somewhat frequently.
 *
 * <p>TODO: Do that analysis.
 *
 * @author dwtj
 */
public class BloomFilterTest {

    @Test
    public void tinySetTestWithBloomFilterDet() {
        tinySetTest(new BloomFilterDet(4, 3));
    }

    @Test
    public void tinySetTestWithBloomFilterRan() {
        tinySetTest(new BloomFilterRan(4, 3));
    }

    @Test
    public void randomSetTestWithBloomFilterDet() {
        int numItems = 1000000;
        randomSetTest(new BloomFilterDet(numItems, 20), numItems, 10, numItems);
    }

    @Test
    public void randomSetTestWithBloomFilterRan() {
        int numItems = 1000000;
        randomSetTest(new BloomFilterRan(numItems, 20), numItems, 10, numItems);
    }

    private static void tinySetTest(BloomFilter filter) {
        String[] strings = {"David", "William", "Travers", "Johnston"};
        for (String s : strings) {
            filter.add(s);
        }
        for (String s : strings) {
            assertTrue(filter.appears(s));
        }
    }

    private static void randomSetTest(BloomFilter filter, int numItems, int minTries, int maxTries) {
        float[] items = new float[numItems];
        Random rand = new Random();
        for (int idx = 0; idx < numItems; idx++) {
            items[idx] = rand.nextFloat();
        }
        Arrays.sort(items);  // Sorted so that later lookups are faster.

        for (float item : items) {
            filter.add(Float.toString(item));
        }
        for (float item : items) {
            assertTrue(filter.appears(Float.toString(item)));
        }

        // Try to find a conflict. Assert that one will be found after many random tries.
        for (int idx = 0; idx < maxTries; idx++) {
            float f = rand.nextFloat();
            int loc = Arrays.binarySearch(items, f);
            if (0 <= loc && loc < items.length) {
                continue;  // This random number `f` happens to already be in the set. Try again.
            }
            if (filter.appears(Float.toString(f))) {
                // After so few tries, we already found a conflict. Something is probably wrong with
                // the implementation.
                assertTrue(idx >= minTries);

                // Otherwise, we have found a conflict, which is what we eventually expect to find.
                return;
            }
        }

        // After all of those tries, we never found a conflict. Something is probably wrong with
        // the implementation.
        assertTrue(false);
    }
}
