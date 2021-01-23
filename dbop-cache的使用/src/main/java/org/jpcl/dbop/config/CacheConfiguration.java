package org.jpcl.dbop.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

/**
 * @author Administrator
 */
@Configuration
public class CacheConfiguration {

    /**
     * 可以有多个 cacheManager 但是实际上整个项目只会使用一个 注解 @Cacheable 可没有参数来指定使用那个 cacheManager
     * Primary 用来说明 这个 cacheManager 为默认使用的 cacheManager
     * @param redisTemplate 因为我们配置的是 RedisCacheManager 所以 redis 你肯定提前配置好了
     * @return
     */
    @Bean
    @Primary
    @DependsOn("redisTemplate")
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {

        RedisCacheConfiguration cacheConfiguration = defaultCacheConfig()
                // 不缓存空值
                .disableCachingNullValues()
                // 设置 key 为 string 序列化 直接使用redis的string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
                // 设置 value 为 redis 的 object 的序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getHashValueSerializer()));
        return RedisCacheManager
               // 使用 redis 的 factory
               .builder(redisTemplate.getRequiredConnectionFactory())
               .cacheDefaults(cacheConfiguration)
               .build();
    }
}
