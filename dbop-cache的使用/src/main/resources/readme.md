# Spring Boot Cache 的使用
## 1. 简单使用
### 1.1 引入依赖
```pom
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```
### 1.2 启动类启用Cache
```java
@SpringBootApplication
@EnableCaching
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }
}
```
### 1.3 测试
```java
@RestController
public class DemoController {

    @Autowired
    private Demo demo;

    @Autowired
    private DefaultCacheService defaultCacheService;

    @GetMapping("/test/{content}.do")
    public String cacheTest1(@PathVariable String content) {
        return demo.get(content);
    }

    @GetMapping("/test1/{content}.do")
    public String cacheTest2(@PathVariable String content) {
        return defaultCacheService.get(content);
    }

    @Component
    public static class Demo {

        /**
         * value 必须要有 指定缓存名称 比如你存储到map中 那么就是给这个map起一个名字 没用运行时保存
         * key-value key可以自己指定 value就是return值了
         */
        @Cacheable(value = "cache1")
        public String get(String content) {
            System.out.println(content);
            return content + " Hello !";
        }
    }
}
```

可以在控制台看见 http://localhost/test/bba.do 第一次调用打印 bba, 再次调用请求之后不再打印。 即缓存生效了。

## 2. 简单介绍
### 2.1 缓存简述
1. 为什么要用缓存  
主要是提高性能, 缓解服务器或数据库压力。
2. 那些数据适合缓存  
访问频率或者说读的频率高, 更新的频率低的数据适合缓存
3. spring boot cache 做了些啥  
spring boot 3.1 之后统一了缓存接口, 整合了各种缓存实现机制(比如缓存到内存中hashmap, 缓存到redis中), 提供了注解(代理)的方式配置缓存。
通常用在get方法上, 当我们使用同样的参数(缓存策略)多次调用方法时, 返回第一次调用的值(首次调用返回值被缓存), 就不用执行方法体逻辑从而提高性能。 
4. 缓存要注意啥
+ 通常要思考那些数据要缓存那些不需要
+ 缓存的数据通常设计成最简单的数据, 不要太复杂
+ 要注意缓存的刷新, 确保缓存中数据和实际应得数据一致性的问题
+ 其它的略
### 2.2 spring boot cache 简述
1. spring boot 提供了那些缓存实现  
针对不同的缓存需要实现不同的 CacheManager
+ ConcurrentMapCache (ConcurrentMapCacheManager)  
默认使用的缓存实现, 存储在内存中, 使用ConcurrentMap作为缓存技术, 需要显式的删除缓存, 无过期机制
+ RedisCache (RedisCacheManager) [推荐]  
使用Redis作为缓存技术.
+ EhCacheCache (EhCacheCacheManager)
使用EhCache(一个纯Java的进程内缓存框架)作为缓存技术.
+ 还有其它的 NoOpCache, GuavaCache, JCacheCache [等等](https://www.imooc.com/article/282071) 需要的话自己查了。
2. 使用 RedisCache 作为缓存实现
```java
@Configuration

public class CacheConfiguration {

    /**
     * 可以有多个 cacheManager
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
```

## 3. 缓存注解
### 3.1 @Cacheable
对方法进行缓存(主要是get方法, 也可以用在类上) 
+ value/cacheNames  
  String[]类型, 缓存命名空间的名字, 可以是多个, 将缓存存储在指定的这一个或多个命名空间中, 由于Spring 4中新增了@CacheConfig, 因此在Spring 3中原本必须有的value属性，也成为非必需项了
+ key  
  String类型, 缓存数据的key, 支持spEL语法, 缺省按照函数的所有参数组合作为key值。
+ keyGenerator  
  String类型,  key的生成器, 和key属性互斥, 自定义 keyGenerator 必须实现org.springframework.cache.interceptor.KeyGenerator,default使用默认的参数值生成器
+ cacheManager  
  String类型, 指定缓存管理器，或者cacheResolver指定获取解析器
+ cacheResolver  
  String类型, 与CacheManager冲突
+ condition  
  String类型, 指定条件满足才缓存, 与unless相反。支持spEL语法
+ unless  
  String类型, 否定缓存, 当满足条件时, 结果不被缓存。可以获取到结果(#result)进行判断。支持spEL语法
+ sync  
  boolean类型, 否异步模式。在该模式下unless不被支持。default=false
### 3.2 @CacheEvict
通常在删除方法上, 
+ allEntries  
  非必需，默认为false。当为true时，会移除所有数据。
+ beforeInvocation 
  非必需，默认为false，会在调用方法之后移除数据。当为true时，会在调用方法之前移除数据。
### 3.3 @CachePut
通常在更新方法上, 每次方法都会被调用, 结果也会被缓存, 与@Cacheable区别在于是否每次都调用方法, 常用于更新数据。
### 3.3 @EnableCaching
开启基于注解的缓存
### 3.4 @CacheConfig
统一配置本类的缓存注解的属性, 常用于类上面, 比如统一缓存命名空间。

## 4. SpEl的使用
|名称|位置|描述|示例|
|----|----|----|----|
|methodName|root对象|当前被调用的方法名|@Cacheable(value = "cache1", key="methodName")或者@Cacheable(value = "cache1", key="#root.methodName")|
|method|root对象|当前被调用的方法|@Cacheable(value = "cache1", key="method.name") 或者 @Cacheable(value = "cache1", key="#root.method.name")|
|target|root对象|当前被调用的目标对象实例, 也就是this指针||
|targetClass|root对象|当前被调用的目标对象的类||
|args|root对象|当前被调用的方法的参数列表|#root.args[0]|
|caches|root对象|当前方法调用使用的缓存列表|#root.caches[0].name|
|Argument Name|执行上下文|当前被调用的方法的参数|findArtisan(Artisan artisan),可以通过#artsian.id获得参数|
|result|执行上下文|方法执行后的返回值(仅当方法执行后的判断有效)|如 unless cacheEvict的beforeInvocation=false|
* 当我们要使用root对象的属性作为key时我们也可以将"#root"省略, 因为Spring默认使用的就是root对象的属性.
* 使用方法参数时我们可以直接使用"#参数名"或者"#p参数index"
* 关系: <，>，<=，>=，==，!=，lt，gt，le，ge，eq，ne
* 算数: +，- ，* ，/，%，^
* 逻辑: &&，||，!，and，or，not，between，instanceof
* 条件: ?: (ternary)，?: (elvis)
* 正则: matches
* 其他类型: ?.，?[…]，![…]，^[…]，$[…]


##注解介绍
1. [springboot @Cacheable 基本使用](https://www.cnblogs.com/ejiyuan/p/11014765.html)
2. [注解的详细使用](https://blog.csdn.net/dreamhai/article/details/80642010)

