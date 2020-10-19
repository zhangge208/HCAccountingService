package com.hardcore.accounting.manager;

import java.util.concurrent.TimeUnit;

public interface CacheManager {
    /**
     * Get the cached value by the specific key.
     * @param key the specific key.
     * @return the cached value.
     */
    Object getValue(String key);

    /**
     * Set the key-value entry into cache.
     * @param key the key of cache entry.
     * @param value the value of cache entry.
     */
    void setValue(String key, Object value);

    /**
     * Set the key-value entry into cache.
     * @param key the key of cache entry.
     * @param value the value of cache entry.
     * @param timeout the key expiration timeout.
     * @param unit must not be {@literal null}.
     */
    void setValueWithTtl(String key, Object value, long timeout, TimeUnit unit);

    /**
     * Delete the value by the specific key.
     * @param key the specific key.
     */
    void delByKey(String key);

}
