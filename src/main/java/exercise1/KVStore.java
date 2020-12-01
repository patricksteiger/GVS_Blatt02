package exercise1;

public interface KVStore {
    String put(String key, String value);
    String get(String key);
    boolean isEmpty();
}
