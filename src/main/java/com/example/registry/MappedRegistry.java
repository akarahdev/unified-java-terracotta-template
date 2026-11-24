package com.example.registry;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MappedRegistry<T> implements Registry<T> {
    private final Map<String, T> map;

    public MappedRegistry(Map<String, T> map) {
        this.map = map;
    }

    @Override
    public @Nullable T getOrNull(String key) {
        return this.map.getOrDefault(key, null);
    }

    @Override
    public List<String> keys() {
        return new TreeSet<>(this.map.keySet()).stream().toList();
    }
}
