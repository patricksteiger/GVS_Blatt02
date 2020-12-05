package exercise1;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 05.12.2020
 *
 *****************************************************************************/

public interface KVStore {
    String put(String key, String value);
    String get(String key);
    boolean isEmpty();
}
