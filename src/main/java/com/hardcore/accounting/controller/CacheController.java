package com.hardcore.accounting.controller;

import com.hardcore.accounting.manager.CacheManager;

import io.swagger.annotations.Api;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("v1.0/cache")
@Api(value = "cache management API")
public class CacheController {
    private final CacheManager cacheManager;

    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Get cache value by the specific cache key.
     * @param cacheKey the specific cache key
     * @return the value to return.
     */
    @GetMapping(path = "/{key}", produces = "application/json", consumes = "application/json")
    public Map<String, Object> getCacheValue(@PathVariable("key") String cacheKey) {
        val result = new HashMap<String, Object>();
        result.put("key", cacheKey);
        result.put("value", cacheManager.getValue(cacheKey));
        return result;
    }

}
