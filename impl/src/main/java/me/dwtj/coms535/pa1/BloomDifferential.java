package me.dwtj.coms535.pa1;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Stream;

import static me.dwtj.coms535.pa1.Database.countLines;
import static me.dwtj.coms535.pa1.Database.retrieveRecordFrom;
import static me.dwtj.coms535.pa1.Database.splitLine;

public class BloomDifferential implements Database {

    private static final int NUM_BITS_PER_ELEMENT = 8;

    private BloomFilter filter = null;

    /**
     * Returns a bloom filter corresponding to the records in the differential file, `DiffFile.txt`.
     */
    public BloomFilter createFilter() {
        if (filter != null) {
            return filter;
        }
        BloomFilter filter = new BloomFilterDet(countLines(DIFF_FILE), NUM_BITS_PER_ELEMENT);
        try (Stream<String> stream = Files.lines(DIFF_FILE)) {
            stream.forEach((l) -> filter.add(splitLine(l).getKey()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.filter = filter;
        return filter;
    }

    /** {@inheritDoc} */
    @Override
    public String retrieveRecord(String key) {
        if (filter == null) {
            createFilter();
        }
        if (filter.appears(key)) {
            Optional<String> value = retrieveRecordFrom(key, DIFF_FILE);
            if (value.isPresent()) {
                return value.get();
            }
            // The value may not be present; we may have hit a false positive in the filter.
        }

        // Fall back to lookup in the whole database file:
        return retrieveRecordFrom(key, DATABASE_FILE).orElseGet(() -> null);
    }
}
