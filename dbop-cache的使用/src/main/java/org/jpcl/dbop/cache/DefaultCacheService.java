package org.jpcl.dbop.cache;

import org.jpcl.dbop.model.Student;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 使用默认缓存时 需要注释掉MyRedisConfig的配置
 * 使用默认的cache
 * 默认是存储在一个ConcurrentMapCache中
 * @author Administrator
 */
@Service
public class DefaultCacheService {

    /**
     * Cacheable
     *    value属性是必须指定 表示当前方法的返回值是会被缓存在哪个Cache上 对应Cache的名称 其可以是一个Cache也可以是多个Cache，当需要指定多个Cache时其是一个数组。
     * @param name
     * @return
     */
    @Cacheable(value = "def_method1")
    public Student method1(String name) {
        System.out.println("method1 方法开始执行...");
        Student student = new Student();
        student.setName(name);
        return student;
    }

    /**
     * Cacheable
     *     key 自定义生成策略  SpringEL表达式
     *     #par  就是代表该par参数  可以使用其属性值
     *     #p2   代表第三个参数 可以使用其属性值  从0开始
     */
    @Cacheable(value = "def_method2", key ="#p0.name+#st.age+#p1")
    public Student method2(Student st, int age) {
        System.out.println("method2 方法开始执行...");
        Student student = new Student();
        student.setName(st.getName());
        student.setAge(age);
        return student;
    }

    /**
     * Cacheable
     *     condition 表示什么时候缓存 什么时候不缓存 默认是空 所有情况都缓存
     *     使用SpringEL表达式来指定的  true 缓存  false 不缓存
     */
    @Cacheable(value = "def_method3", condition = "#p0.age%2 == 0")
    public Student method3(Student st) {
        System.out.println("method3 方法开始执行 年龄偶数缓存  奇数不缓存...");
        Student student = new Student();
        student.setName(st.getName());
        return student;
    }

    /**
     * CachePut
     *    和Cacheable属性一样
     *    Cacheable执行时先检查key有的话直接从缓存中取
     *    cachePut不检查 每次都执行然后存进缓存中
     */
    @CachePut(value = "def_method4", condition = "#p0.age%2 == 0")
    public Student method4(Student st) {
        System.out.println("method4 cacheput每次都执行");
        Student student = new Student();
        student.setName(st.getName());
        return student;
    }

    /**
     * CacheEvict
     *    相同名称的属性和Cacheable属性一样
     *    CacheEvict 清除缓存
     *        allEntries true表示清楚缓存名中的所有缓存
     *        beforeInvocation  执行指定方法成功后清除
     */
    @CacheEvict(value = "def_method4", condition = "#p0.age%2 == 0")
    public Student method5(Student st) {
        System.out.println("method5 清除缓存信息");
        Student student = new Student();
        student.setName(st.getName());
        return student;
    }
}
