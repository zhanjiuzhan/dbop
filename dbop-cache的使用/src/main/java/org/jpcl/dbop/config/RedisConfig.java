package org.jpcl.dbop.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author Administrator
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 默认模板只支持字符串 这里进行自定义
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2Serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // ObjectMapper是Jackson提供的一个类，作用是将java对象与json格式相互转化
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 所有非final类型
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2Serializer.setObjectMapper(om);

        // 设置序列化器
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2Serializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2Serializer);

        // 可以没有 但最好加上
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean(name="stringRedisOps")
    public StringRedisOp<String> getStringRedisOpS() {
        return new StringRedisOp(stringRedisTemplate.opsForValue());
    }

    @Bean(name="stringRedisOp")
    public StringRedisOp getStringRedisOp() {
        return new StringRedisOp(redisTemplate.opsForValue());
    }
}
