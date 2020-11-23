# Spring Boot 集成 Redis
## 集成Redis
### 1. 引入依赖
```maven
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
**附:**  
> 1. 我们都知道 spring boot 简化了配置, 对版本进行了管理。它本身也集成好了redis, 所以当我们使用时直接从父 spring-boot-starter-parent
中引入配置就触发了spring boot 默认的redis配置。
> 2. 我们可以在 spring-boot-dependencies 中找到这个 redis 依赖, 它存在于 dependencyManagement 中。所以是需要我们用的时候自己引入。
> 3. 当我们引入了redis依赖后, 默认的redis配置即生效了, 从 spring-boot-autoconfigure 的 spring.factories 中可以知道 spring boot 启用了
 RedisAutoConfiguration RedisReactiveAutoConfiguration RedisRepositoriesAutoConfiguration 这三个配置类, 所以一引入, 启动时 @ConditionalOnClass
 条件生效, 那么这些配置就生效了, 自然你就已经算是集成好了 redis 了。
> 4. 查看一下依赖 发现引入的时候会引入 spring-boot-start, spring-data-redis, lettuce-core 这三个jar, 第一个是spring相关, 第二个就是redis了, 
第三个应该是 redis 的 lettuce 客户端驱动包。

### 2. redis 的使用
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/get/{id}")
    public String test1(@PathVariable String id) {
        stringRedisTemplate.opsForValue().set("id", id);
        return stringRedisTemplate.opsForValue().get("id");
    }
}
```
**附:** 
> 1. 没有看错, 引入依赖之后直接就能用了, StringRedisTemplate 是对 redis string 类型的操作, 这个还是很好的不用做任何变动。操作 string 
类型时直接就用。
> 2. 为什么能用呢, 那就是自动化配置了, RedisProperties 中默认 host是localhost, port是6379, 密码没有, 其它默认自己看了, 这不刚好契合我本地redis配置。
> 3. RedisAutoConfiguration 中也给我们提供了 RedisTemplate 和 StringRedisTemplate 两个bean, 前者操作对象, 后者就是字符串。 
> 4. 至于 RedisConnectionFactory, 默认是用的 lettuce 客户端。
> 5. 当然你可以使用 其它的客户端比如 jedis和redisson , RedisTemplate 存储对象时的编码你也可以自定义。

## Redis 配置
### 1. 简单配置
```yml
spring:
  redis:
    host: localhost
    port: 6379
    password:
```
**附:** 
> 1. 上面集成 redis 的时候我们没有配置啥, 那是使用的默认。 配置类 RedisProperties。
> 2. 现在要自己配置了, 如上就欧克, 在你的application.yml中这样配置即可。 当然这样配置的只是单纯的连接了一个redis服务噢。

### 2. 使用连接池
```maven
<dependency>
    <!-- 资料 https://www.open-open.com/lib/view/open1415453575730.html -->
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.5.0</version>
</dependency>
```
```yml
spring:
  redis:
    lettuce:
      pool:
        # 连接池最大连接数(使用负值表示没有限制) 默认为8
        max-active: 8
        # 连接池最大阻塞等待时间(使用负值表示没有限制) 默认为-1
        max-wait: -1ms
        # 连接池中的最大空闲连接 默认为8
        max-idle: 8
        # 连接池中的最小空闲连接 默认为 0
        min-idle: 0
```
**附:** 
> 1. 为啥要用连接池, 我们都知道我们连接redis通过tcp, 不能每次连接都重新创建一次tcp连接吧, 这个需要开销, 所以推出连接池对这种连接进行管理。
> 2. 要添加 commons-pool2 这个依赖, 它是个对象池, 这里是用来管理redis连接对象的, 当然也可以管理其它的并不只是为redis服务, 只是redis用到它。
> 3. 这里是 lettuce.pool 当然你也可以是 jedis.pool, 在springboot1.5的时候默认用的是jedis, 2.0之后就是默认lettuce了。
> 4. jedis, lettuce 还有个 Redisson 这个叫做redis客服端, 就是用代码来进行redis的命令, 提交数据到服务。
> 5. redis 默认是有 lettuce 和 jedis的, 想用jedis那么在 spring-boot-starter-data-redis 中 exclusion 掉 lettuce-core 这个依赖就行。

### 3. 其它配置
```yml
spring:
  redis:
    # 主从 哨兵
    sentinel:
      # 主服务名称
      master: mymaster
      nodes:
        - 127.0.0.2:6379
        - 127.0.0.3:6379
    # 集群
    cluster:
      # 最大重定向数
      max-redirects: 10
      nodes:
        - 127.0.0.2:6379
        - 127.0.0.3:6379
```
**附:** 
> 1. 其它的配置 这里说的是 提供给我们 RedisProperties 这个配置类所能提供的配置。
> 2. 若要更为详细的自定义配置那么就不能使用 RedisProperties 这个配置类了, 且需要自己实现factory。 当使用多数据源或者更详细的个性化配置时我们需要自己搞咯。

## 序列化
序列化干啥, 我们都知道把对象存储到mysql中需要字段一一对应mysql中的列, 那么把对象存储到 redis 中就需要将对象序列化了, 取出来的时候再反序列化。
### StringRedisTemplate
这个上面已经用过了, 看名字就是专门操作string类型的, 对应于 redis 中的string类型。 默认使用的是 StringRedisSerializer 序列化器。其实这个序列化器也没干啥。  
StringRedisTemplate 继承 RedisTemplate<String, String> 里面指定StringRedisSerializer为序列化器。
### RedisTemplate
RedisTemplate 是干啥呢, 它可以看作是提供给我们的句柄, 专门用来操作 redis 的指令的。基本上的 redis 指令都可以用它的方法来替代执行。  
我们可以再 RedisTemplate 中找到默认的序列化器是 JdkSerializationRedisSerializer 。  
#### 集成的redis中给我们提供的序列化器有: 
1. StringRedisSerializer(推荐)
> 专用于字符串, 保存redis中和java的字符串字面值表现一样。

2. Jackson2JsonRedisSerializer(推荐)  
> 将对象序列化成Json. 一旦这样使用后，要修改对象的一个属性值时，就需要把整个对象都读取出来，再保存回去。
> 序列化的对象不用实现 Serializable 接口。
> 必须提供序列化类型对象信息, 序列化后该类型信息也会被保留用于反序列化。
> 在集合泛型这里可能也会有问题, 我么有深究, 用到再说。

3. GenericToStringSerializer(不推荐) 
> 使用Spring转换服务进行序列化, 需要传转换器去做。

4. GenericJackson2JsonRedisSerializer(推荐) 
> 和Jackson2JsonRedisSerializer 差不多, 比 Jackson2JsonRedisSerializer 性能差点。
> 这个通用的序列化器, 会丢失泛型, 所以list, set, long都需要注意, 比如long反序列化可能变成int。

5. JdkSerializationRedisSerializer(默认)
> 默认序列化器, 底层是通过调用JDK的IO操作ObjectInputStream和ObjectOutputStream类实现POJO的序列化和反序列化。
> key和value都采用的这个序列化器, 所以存储在redis中的话那么将看到的是 \xAC\xED\x00\x05t\x00\x04 这样的。
> 序列化的POJO必须实现 Serializable 接口, 使用transient 可以控制那些字段不被序列化。
> 对开发者不友好, 序列化后数据不易阅读且数据量比json庞大。
 
6. OxmSerializer :   
> 使用SpringO/X映射的编排器和解排器实现序列化，用于XML序列化。

### 自定义序列化器
我们知道默认使用的是JdkSerializationRedisSerializer, 它存储在redis中的数据是看不懂的。 所以你可能需要指定可序列化器, 让存储的数据方便阅读, 也可以手动添加。
那么我们就需要自定义实现个 redisTemplate bean了, 当这个bena存在的时候默认的 redisTemplate 将不再注入容器中。
```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
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
}
```
**附:** 
> 1. 其实自定义序列化器我们可以直接模仿 StringRedisTemplate 。
> 2. RedisConnectionFactory 已经被容器管理了, 上面有说过, 2.x 是 LettuceConnectionFactory。

## 使用
通过上面, 已经对redis的使用大有了解了。这里补充一点。
> 1. 使用序列化器可能会有问题, 但是基本是没问题的除非你的数据类型特别复杂。
> 2. 序列化器, 除了自带的, 第三方也有提供, 这个自己查询使用, 比如fastjson。
> 3. 无论怎样都会存在 stringRedisTemplate 和 redisTemplate 这两个bean, 后者你自定义序列化器覆盖, 不然它还是存在的。
> 4. 操作字符串就用 stringRedisTemplate, 你甚至可以把对象变成json字符串用 stringRedisTemplate。
> 5. StringRedisTemplate 继承 RedisTemplate, RedisTemplate 可以操作redis了, 所谓的redis工具其实也就是封装它, 让方法看起来和redis执行命令一模一样。
```java
// redisTemplate 本身可以取得所有key 删除key  设置生命周期等
// redisTemplate.opsForValue() 取得 ValueOperations 用来操作 string 类型
// redisTemplate.opsForList() 取得 ListOperations 用来操作 list 类型
// redisTemplate.opsForSet() 取得 SetOperations 用来操作 set 类型
// redisTemplate.opsForZSet() 取得 ZSetOperations 用来操作 zset 类型
// redisTemplate.opsForHash() 取得 DefaultHashOperations 用来操作 hash 类型
```
