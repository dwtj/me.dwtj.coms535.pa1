package me.dwtj.coms535.pa1;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author dwtj
 */
public interface Database {

    Path DATABASE_FILE = Paths.get("data", "database.txt");
    Path DIFF_FILE = Paths.get("data", "DiffFile.txt");
    Path GRAMS_FILE = Paths.get("data", "grams.txt");

    /**
     * Returns the value associated with this key, or else the {@code null} if this key was not
     * found in the database.
     */
    String retrieveRecord(String key);

    /**
     * Splits the given line in the database file into a key-value pair.
     */
    static Map.Entry<String, String> splitLine(String line) {
        // TODO: Make this method less gross!
        int count = 0;
        int cur = 0;
        do {
            cur = line.indexOf(' ', cur+1);  // Move cursor to next space character.
            count++;
        } while (0 <= cur && count < 4);

        String key, value;
        if (0 <= cur && cur < line.length()) {
            // Don't include the separating space in either `key` or `value`:
            key = line.substring(0, cur);                  //
            value = line.substring(cur+1, line.length());
        } else {
            key = line;
            value = "";
        }

        return new Map.Entry<String, String>() {
            @Override public String getKey() {
                return key;
            }
            @Override public String getValue() {
                return value;
            }
            @Override public String setValue(String value) {
                throw new UnsupportedOperationException();
            }
        };
    }

    static Optional<String> retrieveRecordFrom(String key, Path path) {
        try (Stream<String> stream = Files.lines(path)) {
            return stream.filter((l) -> key.equals(splitLine(l).getKey()))
                    .findFirst();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    static int countLines(Path path) {
        try (LineNumberReader reader = new LineNumberReader(new FileReader(path.toFile()))) {
            while (reader.readLine() != null) { }
            return reader.getLineNumber();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    static boolean isInDiffFile(String key) {
        try (Stream<String> stream = Files.lines(DIFF_FILE)) {
            return stream.anyMatch((l) -> key.equals(splitLine(l).getKey()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
