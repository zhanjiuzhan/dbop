# Spring Boot Redis 多数据源
## 1. 添加多数据源配置
```java
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Redis 多数据源配置类
 * @author Administrator
 */
@Configuration
// 当依赖 spring-data-redis 时这个配置类才生效
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties({AutoRedisConfiguration.JpclRedisProperties.class})
public class AutoRedisConfiguration  {

    private static Logger logger = LoggerFactory.getLogger(AutoRedisConfiguration.class);

    /**
     * 配置文件配置参数注入
     */
    @ConfigurationProperties(prefix = "spring")
    public static class JpclRedisProperties {

        // 多数据源所以是 map 了, RedisProperties 这个是 spring boot 本身的一个redis 配置类 可以直接用
        private Map<String, RedisProperties> multipleRedis;

        public Map<String, RedisProperties> getMultipleRedis() {
            return multipleRedis;
        }

        public void setMultipleRedis(Map<String, RedisProperties> multipleRedis) {
            this.multipleRedis = multipleRedis;
        }
    }

    /**
     * 这里来根据配置文件的配置注册响应的 RedisTemplate Bean 到容器中
     */
    @Component
    public static class RegisterBean implements ApplicationContextAware, InitializingBean {

        private ApplicationContext context;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            // 取得配置信息
            JpclRedisProperties properties = this.context.getBean(JpclRedisProperties.class);
            // 取得 ioc bean 工厂
            DefaultListableBeanFactory factory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext )this.context).getBeanFactory();
            // 配置信息存在才进行bean的注册
            if (properties.getMultipleRedis() != null && properties.getMultipleRedis().size() > 0) {
                for (Map.Entry<String, RedisProperties> element : properties.getMultipleRedis().entrySet()) {
                    // 创建一个 RedisTemplate 类型的 bean 的定义
                    BeanDefinitionBuilder beanDefine =  BeanDefinitionBuilder.genericBeanDefinition(RedisTemplate.class);
                    // 因为这个bean会有自己的生命周期 所以它会走到 RedisTemplate 的 afterPropertiesSet 方法, 所以它是必须要一个工厂才行
                    beanDefine.addPropertyValue("connectionFactory", getRedisConnectionFactory(element.getValue()));
                    // 注册该bean到容器中 beanName 起名为自定义 也就是map的key
                    factory.registerBeanDefinition(element.getKey(), beanDefine.getRawBeanDefinition());

                    // 下面是取到你注册到ioc中的bean 给它配置一下 序列化器 当然你也可以不管
                    logger.info("Redis: [baneName: " + element.getKey() + ", data: " + element.getValue().toString() + "] 注册为bean");
                    RedisTemplate template = factory.getBean(element.getKey(), RedisTemplate.class);
                    reconfigure(template);
                    logger.info("Redis: [baneName: " + element.getKey() + " 属性配置完成!]");
                }
            }
        }

        /**
         * 取得一个redis 工厂
         * @param properties
         * @return
         */
        private RedisConnectionFactory getRedisConnectionFactory(RedisProperties properties) {
            // 连接池配置
            LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder =  LettucePoolingClientConfiguration.builder();

            // 根据配置信息 设置连接池参数
            if (properties.getLettuce() != null && properties.getLettuce().getPool() != null) {
                GenericObjectPoolConfig genericObjectPoolConfig =
                        new GenericObjectPoolConfig();
                genericObjectPoolConfig.setMaxIdle(properties.getLettuce().getPool().getMaxIdle());
                genericObjectPoolConfig.setMinIdle(properties.getLettuce().getPool().getMinIdle());
                genericObjectPoolConfig.setMaxWaitMillis(properties.getLettuce().getPool().getMaxWait().toMillis());
                genericObjectPoolConfig.setMaxTotal(properties.getLettuce().getPool().getMaxActive());
                builder.poolConfig(genericObjectPoolConfig);
            }
            LettuceClientConfiguration lettuceClientConfiguration = builder.build();

            // 创建工厂
            LettuceConnectionFactory lettuceConnectionFactory = createLettuceConnectionFactory(properties, lettuceClientConfiguration);
            lettuceConnectionFactory.afterPropertiesSet();
            return lettuceConnectionFactory;
        }

        /**
         * 根据配置信息做成redis 工厂
         * @param properties
         * @param clientConfiguration
         * @return
         */
        private LettuceConnectionFactory createLettuceConnectionFactory(RedisProperties properties, LettuceClientConfiguration clientConfiguration) {
            /**
             * 配置的是哨兵模式
             */
            if (getSentinelConfig(properties) != null) {
                return new LettuceConnectionFactory(Objects.requireNonNull(getSentinelConfig(properties)), clientConfiguration);
            }
            /**
             * 配置的是集群模式
             */
            if (getClusterConfiguration(properties) != null) {
                return new LettuceConnectionFactory(Objects.requireNonNull(getClusterConfiguration(properties)), clientConfiguration);
            }
            /**
             * 通用配置模式
             */
            return new LettuceConnectionFactory(getStandaloneConfig(properties), clientConfiguration);
        }

        /**
         * sentinel 哨兵配置信息做成
         * @param properties
         * @return
         */
        protected final RedisSentinelConfiguration getSentinelConfig(RedisProperties properties) {
            RedisProperties.Sentinel sentinelProperties = properties.getSentinel();
            if (sentinelProperties != null) {
                RedisSentinelConfiguration config = new RedisSentinelConfiguration();
                config.master(sentinelProperties.getMaster());
                config.setSentinels(createSentinels(sentinelProperties));
                if (properties.getPassword() != null) {
                    config.setPassword(RedisPassword.of(properties.getPassword()));
                }
                return config;
            }
            return null;
        }

        /**
         * cluster 集群配置信息做成
         * @param properties
         * @return
         */
        protected final RedisClusterConfiguration getClusterConfiguration(RedisProperties properties) {
            if (properties.getCluster() == null) {
                return null;
            }
            RedisProperties.Cluster clusterProperties = properties.getCluster();
            RedisClusterConfiguration config = new RedisClusterConfiguration(
                    clusterProperties.getNodes());
            if (clusterProperties.getMaxRedirects() != null) {
                config.setMaxRedirects(clusterProperties.getMaxRedirects());
            }
            if (properties.getPassword() != null) {
                config.setPassword(RedisPassword.of(properties.getPassword()));
            }
            return config;
        }

        /**
         * 通用模式 配置信息做成 注: 这里弃 url 配置
         * @param properties
         * @return
         */
        protected final RedisStandaloneConfiguration getStandaloneConfig(RedisProperties properties) {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(properties.getHost());
            config.setPort(properties.getPort());
            config.setPassword(RedisPassword.of(properties.getPassword()));
            config.setDatabase(properties.getDatabase());
            return config;
        }

        /**
         * 哨兵模式 copy的源码 可以参照 RedisConnectionConfiguration
         * @param sentinel
         * @return
         */
        private List<RedisNode> createSentinels(RedisProperties.Sentinel sentinel) {
            List<RedisNode> nodes = new ArrayList<>();
            for (String node : sentinel.getNodes()) {
                try {
                    String[] parts = StringUtils.split(node, ":");
                    Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                    nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
                }
                catch (RuntimeException ex) {
                    throw new IllegalStateException(
                            "Invalid redis sentinel " + "property '" + node + "'", ex);
                }
            }
            return nodes;
        }

        /**
         * 配置redis的序列化器
         * @param template
         */
        private void reconfigure(RedisTemplate<String, Object> template) {
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            Jackson2JsonRedisSerializer<Object> jackson2Serializer = new Jackson2JsonRedisSerializer<>(Object.class);

            // ObjectMapper是Jackson提供的一个类，作用是将java对象与json格式相互转化
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            // 所有非final类型
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2Serializer.setObjectMapper(om);

            // 设置序列化器
            template.setKeySerializer(stringRedisSerializer);
            template.setValueSerializer(jackson2Serializer);
            template.setHashKeySerializer(stringRedisSerializer);
            template.setHashValueSerializer(jackson2Serializer);

            // 可以没有 但最好加上
            template.afterPropertiesSet();
        }
    }
}
```
> 上面的代码根据配置文件信息配置了多数据源, 可以直接使用不用修改, 放到项目能扫描到的地方即可。

## 2. 配置
```
spring:
  multiple-redis:
    redis:
      host: 127.0.0.1
      port: 6379
    redis1:
      cluster:
        max-redirects: 10
        nodes:
          - 192.168.88.127:6381
          - 192.168.88.127:6382
          - 192.168.88.127:6383
          - 192.168.88.127:6384
          - 192.168.88.127:6385
          - 192.168.88.127:6386
    redis2:
      sentinel:
        master: mymaster
        nodes:
          - 192.168.88.128:26379
          - 192.168.88.128:26380
      lettuce:
        pool:
          max-active: 8
          max-wait: -1ms
          max-idle: 8
          min-idle: 0
```
> 如上配置了三个数据源, 一个是redis, 一个是 redis1 集群, 一个是 redis2 哨兵。 redis redis1 redis2 这个名字你可以随便起, 至于下面的参数和 spring boot redis 配置参数一模一样。 

## 3. 使用
```java
@RestController
public class TestController {
    @Resource
    private RedisTemplate redis;

    @Resource
    private RedisTemplate redis1;

    @Resource
    private RedisTemplate redis2;

    @GetMapping("/test")
    public Object test() {
        redis.opsForValue().set("test", "redis");
        return redis.opsForValue().get("test");
    }

    @GetMapping("/test1")
    public Object test1() {
        redis1.opsForValue().set("test", "redis1");
        return redis1.opsForValue().get("test");
    }

    @GetMapping("/test2")
    public Object test2() {
        redis2.opsForValue().set("test", "redis2");
        return redis2.opsForValue().get("test");
    }
}
```
> 如上直接可以使用了, 只不过 RedisTemplate 需要根据 beanName 来取, beanName 就是你配置时起的那个名字, 所以不要重复哈。

## 4. 依赖
多数据源和但数据源依赖是一样的
```pom
    <dependencies>
        <!-- 核心的spring boot依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--redis依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
    </dependencies>
```
