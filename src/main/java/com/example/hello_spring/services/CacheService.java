package com.example.hello_spring.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine-based in-memory cache service that provides a simple key-value store.
 * Configured as a Spring singleton service with sensible defaults for TTL and size limits.
 */
@Service
public class CacheService {

    private final Cache<String, Object> cache;

    /**
     * Constructor initializes the Caffeine cache with sensible defaults:
     * - 10 minutes TTL (Time To Live)
     * - Maximum size of 1000 entries
     * - Size-based eviction when limit is reached
     */
    public CacheService() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)  // 10-minute TTL as specified
                .maximumSize(1000)                       // Reasonable size limit
                .build();
    }

    /**
     * Store a key-value pair in the cache.
     * 
     * @param key   The cache key (must not be null)
     * @param value The value to cache (cannot be null - Caffeine doesn't support null values)
     */
    public void put(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Cache key cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Cache value cannot be null");
        }
        cache.put(key, value);
    }

    /**
     * Retrieve a value from the cache by key.
     * 
     * @param key The cache key to look up
     * @return The cached value, or null if not found or expired
     */
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        return cache.getIfPresent(key);
    }

    /**
     * Remove a key-value pair from the cache.
     * 
     * @param key The cache key to remove
     */
    public void delete(String key) {
        if (key == null) {
            return;
        }
        cache.invalidate(key);
    }

    /**
     * Get the current size of the cache (number of cached entries).
     * 
     * @return Number of entries currently in the cache
     */
    public long size() {
        return cache.estimatedSize();
    }

    /**
     * Clear all entries from the cache.
     */
    public void clear() {
        cache.invalidateAll();
    }
}