package com.hardcore.accounting.manager;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheManagerImpl implements CacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    public CacheManagerImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object getValue(String key) {
        val value = redisTemplate.opsForValue().get(key);
        log.debug("From redis - key: {}, value: {}", key, value);
        return value;
    }

    @Override
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setValueWithTtl(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, 1, TimeUnit.MINUTES);
    }

    @Override
    public void delByKey(String key) {
        redisTemplate.delete(key);
    }
}
