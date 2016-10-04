package me.dwtj.coms535.pa1;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Arrays.sort;
import static me.dwtj.coms535.pa1.Database.GRAMS_FILE;
import static me.dwtj.coms535.pa1.Database.countLines;
import static me.dwtj.coms535.pa1.Database.isInDiffFile;

public class EmpiricalComparison {

    private static Random random = new Random();

    public static void main(String args[]) {
        out.println("Starting comparison of `NaiveDifferential` and  `BloomDifferential`...");

        out.println("Initializing databases...");
        NaiveDifferential naiveDatabase = new NaiveDifferential();
        BloomDifferential bloomDatabase = new BloomDifferential();
        BloomFilter bloomFilter = bloomDatabase.createFilter();

        out.println("Counting the total number of keys...");
        int numKeys = countLines(GRAMS_FILE);
        int numQueries = numKeys / 10000;
        if (numQueries <= 0) {
            throw new RuntimeException("Not enough queries.");
        }

        out.println(format("Randomly selecting %d query keys...", numQueries));
        int[] indices = random.ints(numQueries, 0, numKeys).toArray();
        sort(indices);
        List<String> queryKeys = getSelectedKeys(indices);
        out.println("A random example query key: "+queryKeys.get(random.nextInt(queryKeys.size())));

        out.println();  // Print potential CSV Header:
        out.println("NaiveDifferential Duration (ms),BloomDifferential Duration (ms),"
                  + "InDiffFile?,InFilter?");
        for (String key : queryKeys) {
            out.println(format("%d,%d,%b,%b",
                        timeQueryDuration(naiveDatabase, key).toMillis(),
                        timeQueryDuration(bloomDatabase, key).toMillis(),
                        isInDiffFile(key),
                        bloomFilter.appears(key)));
        }

        out.println();
        out.println("Empirical comparison complete.");
    }


    private static Duration timeQueryDuration(Database db, String key) {
        Instant start = Instant.now();
        String value = db.retrieveRecord(key);
        Instant end = Instant.now();
        if (value == null) {
            throw new RuntimeException("Expected database to always find records.");
        }
        return Duration.between(start, end);
    }

    /**
     * Gets the selected keys from {@link Database#GRAMS_FILE} file, where values in the argument
     * indicate line numbers of the file.
     *
     * @param selections
     *          A <em>sorted</em> array of integers, which are all valid indices into the keys
     *          stored in {@link Database#GRAMS_FILE}, that is, whose values fall between zero
     *          (inclusive) and the number of keys in this file (exclusive).
     *
     * @return
     *          A list of the keys which were identified in the argument. For every element of the
     *          argument array, there should be a corresponding string the the return value. Exactly
     *          what this corresponding string is depends upon the contents of the
     *          {@link Database#GRAMS_FILE} file.
     */
    private static List<String> getSelectedKeys(int[] selections) {
        try (Stream<String> stream = Files.lines(GRAMS_FILE)) {
            List<String> list = new ArrayList<>(selections.length);

            Iterator<String> iter = stream.iterator();
            int iterIdx = 0;
            String cur = iter.next();

            for (int selection : selections) {
                while (selection != iterIdx) {
                    // Keep incrementing until the current key `selection` is found.
                    if (! iter.hasNext()) {
                        String msg = "Reached the end of the stream of keys unexpectedly.";
                        throw new IllegalStateException(msg);
                    } else {
                        iterIdx++;
                        cur = iter.next();
                    }
                }
                // Now `selection == iterIdx`, so the current key should be added to our `list`.
                list.add(cur);
                // The next `selection` may be the same, so we do not yet increment our iterator.
            }
            return list;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
