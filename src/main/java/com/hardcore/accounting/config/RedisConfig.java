package com.hardcore.accounting.config;

import com.hardcore.accounting.shiro.serializer.ObjectSerializer;

import lombok.val;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final Duration TTL = Duration.ofSeconds(60);
    /**
     * Bean for redis cache manager.
     *
     * @param redisConnectionFactory redis connection factory.
     * @return redis cache manager.
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //return RedisCacheManager.create(redisConnectionFactory);

        val genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                                   .fromSerializer(stringRedisSerializer))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                                     .fromSerializer(genericJackson2JsonRedisSerializer))
            .entryTtl(TTL);
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
    }

    /**
     * Redis Template.
     *
     * @param factory factory.
     * @return redis template.
     **/
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        val template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);

        val genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashKeySerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.setDefaultSerializer(genericJackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * The bean for session redis template.
     * @param factory the connection factory.
     * @return session redis template
     */
    @Bean("sessionRedisTemplate")
    public RedisTemplate<String, Object> sessionRedisTemplate(RedisConnectionFactory factory) {
        val template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);

        val genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new ObjectSerializer());
        template.setHashKeySerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.setDefaultSerializer(genericJackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
