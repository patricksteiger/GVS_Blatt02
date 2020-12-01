package exercise1;

public interface KVStore {
    void put(String key, String value);
    String get(String key);
    boolean isEmpty();
}
