package com.assignment.errornoti.cache;

import org.ehcache.Cache;
import org.ehcache.expiry.ExpiryPolicy;

import java.time.Duration;
import java.util.function.Supplier;

public class CacheExpiryPolicy implements ExpiryPolicy{
    @Override
    public Duration getExpiryForCreation(Object key, Object value) {
        //생성 후 1분뒤 소멸
        return Duration.ofMinutes(1);
    }

    /**
     * Returns the expiration {@link Duration duration} (relative to the current time) when an existing entry
     * is accessed from a {@link Cache Cache}.
     * <p>
     * Returning {@code null} indicates that the expiration time remains unchanged.
     * <p>
     * Exceptions thrown from this method will be swallowed and result in the expiry duration being
     * {@link Duration#ZERO ZERO}.
     *
     * @param key   the key of the accessed entry
     * @param value a value supplier for the accessed entry
     * @return an expiration {@code Duration}, {@code null} means unchanged
     */
    @Override
    public Duration getExpiryForAccess(Object key, Supplier value) {
        return Duration.ofSeconds(10);
    }

    /**
     * Returns the expiration {@link Duration duration} (relative to the current time) when an existing entry
     * is updated in a {@link Cache Cache}.
     * <p>
     * Returning {@code null} indicates that the expiration time remains unchanged.
     * <p>
     * Exceptions thrown from this method will be swallowed and result in the expiry duration being
     * {@link Duration#ZERO ZERO}.
     *
     * @param key      the key of the updated entry
     * @param oldValue a value supplier for the previous value of the entry
     * @param newValue the new value of the entry
     * @return an expiration {@code Duration}, {@code null} means unchanged
     */
    @Override
    public Duration getExpiryForUpdate(Object key, Supplier oldValue, Object newValue) {
        return Duration.ofSeconds(10);
    }
}
