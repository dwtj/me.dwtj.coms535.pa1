package me.dwtj.coms535.pa1;

import static me.dwtj.coms535.pa1.Database.retrieveRecordFrom;

public class NaiveDifferential implements Database {
    @Override
    public String retrieveRecord(String key) {
        return retrieveRecordFrom(key, DIFF_FILE)
                 .orElseGet(() -> retrieveRecordFrom(key, DATABASE_FILE)
                 .orElseGet(() -> null));
    }
}