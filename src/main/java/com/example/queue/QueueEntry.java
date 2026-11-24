package com.example.queue;

public record QueueEntry(
        int id,
        String method,
        String endpoint,
        String body
) {
}
