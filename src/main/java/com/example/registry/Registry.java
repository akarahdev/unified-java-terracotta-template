package com.example.registry;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.NoSuchElementException;

public interface Registry<T> {
    @Nullable T getOrNull(String key);

    List<String> keys();

    default int size() {
        return this.keys().size();
    }

    default boolean has(String key) {
        return this.getOrNull(key) != null;
    }

    default T getOrThrow(String key) {
        T possible = this.getOrNull(key);
        if(possible == null) {
            throw new NoSuchElementException();
        }
        return possible;
    }

    default T getOrDefault(String key, T other) {
        T possible = this.getOrNull(key);
        return possible == null ? other : possible;
    }
}
