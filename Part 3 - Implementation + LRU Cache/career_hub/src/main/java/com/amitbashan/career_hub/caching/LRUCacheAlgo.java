package com.amitbashan.career_hub.caching;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class LRUCacheAlgo<K, V> implements CacheAlgo<K, V> {
    private final int capacity;
    private int size;
    private HashMap<K, Pair<Long, V>> map;

    public LRUCacheAlgo() {
        this(20);
    }

    public LRUCacheAlgo(int capacity) {
        size = 0;
        this.capacity = capacity;
        map = HashMap.newHashMap(capacity);
    }

    public void showCacheContent() {
        List<String> values = map.values()
                .stream()
                .sorted((a, b) -> a.getFirst().compareTo(b.getFirst()))
                .map(Pair::getSecond)
                .map(Object::toString)
                .toList();
        System.out.println(String.join(", ", values));
    }

    private K leastRecentlyUsed() {
        K min = null;
        Long minHits = Long.MAX_VALUE;

        for (Map.Entry<K, Pair<Long, V>> entry : map.entrySet()) {
            Long hits = getHits(entry.getKey());
            if (hits < minHits) {
                minHits = hits;
                min = entry.getKey();
            }
        }

        return min;
    }

    private Long getHits(K key) {
        Pair<Long, V> pair = map.get(key);

        if (pair == null) {
            return null;
        }

        return pair.getFirst();
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > capacity) {
            map.remove(leastRecentlyUsed());
            size -= 1;
        }
        size += 1;
        map.replaceAll((_, v) -> Pair.of(v.getFirst() + 1, v.getSecond()));
        map.put(key, Pair.of(0L, value));
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        Pair<Long, V> pair = map.get(key);
        map.put(key, Pair.of(pair.getFirst() + 1, pair.getSecond()));
        return pair.getSecond();
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public int size() {
        return size;
    }
}
