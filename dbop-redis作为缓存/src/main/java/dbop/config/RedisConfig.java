package dbop.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private Environment environment;
    /**
     * 默认 JdkSerializationRedisSerializer
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
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


    /** #######################################################################
     *  配置 redis1
     *  #######################################################################
     */
    private LettuceConnectionFactory getLettuceConnectionFactory() {
        //redis配置
        RedisStandaloneConfiguration redisConfiguration = new
                RedisStandaloneConfiguration(environment.getProperty("spring.redis1.host"),
                Integer.valueOf(environment.getProperty("spring.redis1.port", "6379")));
        redisConfiguration.setDatabase(Integer.valueOf(environment.getProperty("spring.redis1.database", "0")));

        //连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig =
                new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(Integer.valueOf(environment.getProperty("spring.redis1.lettuce.pool.max-idle", "0")));
        genericObjectPoolConfig.setMinIdle(Integer.valueOf(environment.getProperty("spring.redis1.lettuce.pool.min-idle", "0")));
        genericObjectPoolConfig.setMaxWaitMillis(Integer.valueOf(environment.getProperty("spring.redis1.lettuce.pool.max-wait", "0")));
        genericObjectPoolConfig.setMaxTotal(Integer.valueOf(environment.getProperty("spring.redis1.lettuce.pool.max-active", "0")));

        //redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder =  LettucePoolingClientConfiguration.builder();
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        //根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);
        lettuceConnectionFactory .afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    @Bean("redisTemplate1")
    public RedisTemplate<String, Object> redisTemplate1() {
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
        redisTemplate.setConnectionFactory(getLettuceConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean(name="stringRedisOp1")
    public StringRedisOp getStringRedisOp1() {
        return new StringRedisOp(redisTemplate1().opsForValue());
    }

    private RedisTemplate<String, Object> getRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置key
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // 设置值
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        return redisTemplate;
    }
}
