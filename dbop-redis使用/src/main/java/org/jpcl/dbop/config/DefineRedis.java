package org.jpcl.dbop.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
public class DefineRedis<T> {
    private ValueOperations<String, T> valueOperations;
    private RedisTemplate template;

    public DefineRedis(@NonNull RedisTemplate template) {
        this.template = template;
        this.valueOperations = template.opsForValue();
    }

    /**
     * 设置一个值
     * @param key  最大512M  按照hash查询的
     * @param value 最大512M
     */
    public void set(@NonNull String key, @NonNull T value) {
        this.valueOperations.set(key, value);
    }

    @Nullable
    public T get(String key) {
        return this.valueOperations.get(key);
    }


    public Set<String> keys(String pattern) {
        return this.template.keys(pattern);
    }

    /**
     * 设置过去时间 秒
     * @param second
     */
    public void expire(@NonNull String key, long second) {
        template.expire(key, second, TimeUnit.SECONDS);
    }

    public void pexpire(@NonNull String key, long millSecond) {
        template.expire(key, millSecond, TimeUnit.MILLISECONDS);
    }

    /**
     * 判断指定的zset中是否包含该成员
     * @param key
     * @param member
     * @return
     */
    public boolean zmexists(String key, String member) {
        Double score = zscore(key, member);
        if (score == null) {
            return false;
        }
        return true;
    }

    /**
     * 取得zset中该成员的score值
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
        return template.opsForZSet().score(key, member);
    }

    /**
     * 给成员添加一定的增量
     * @param key
     * @param member
     * @param increment
     */
    public void zincrby(String key, String member, double increment) {
        template.opsForZSet().incrementScore(key, member, increment);
    }

    /**
     * 根据由大到小的顺序返回
     * @param key
     * @param start 第一个元素 从0开始
     * @param end 第end+1个元素
     */
    public Set<String> zrevrange(String key, long start, long end) {
        return template.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 讲一个值或者多个值 插入到列表头部
     * @param key
     * @param values
     * @return
     */
    public long lpush(String key, T... values) {
        return template.opsForList().leftPushAll(key, values);
    }

    /**
     * 取出指定位置索引的值
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<T> lrange(String key, int start, int end) {
        return template.opsForList().range(key, start, end);
    }
}
