package dbop.config;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Redis String类型的操作
 * @author Administrator
 */
public class StringRedisOp<T> {
    private ValueOperations<String, T> valueOperations;

    public StringRedisOp(@NonNull ValueOperations<String, T> valueOperations) {
        this.valueOperations = valueOperations;
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
}
