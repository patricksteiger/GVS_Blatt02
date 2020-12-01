package exercise1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteKVStore implements KVStore {
    private final Map<String, String> store;

    public RemoteKVStore() {
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public String put(String key, String value) {
        return this.store.put(key, value);
    }

    @Override
    public String get(String key) {
        return this.store.get(key);
    }

    @Override
    public boolean isEmpty() {
        return this.store.isEmpty();
    }
}
