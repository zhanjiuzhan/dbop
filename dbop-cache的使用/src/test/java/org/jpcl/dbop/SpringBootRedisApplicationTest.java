package org.jpcl.dbop;

import org.jpcl.dbop.cache.DefaultCacheService;
import org.jpcl.dbop.cache.RedisCacheService;
import org.jpcl.dbop.config.StringRedisOp;
import org.jpcl.dbop.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRedisApplicationTest {

    @Autowired
    @Qualifier("stringRedisOp")
    private StringRedisOp stringRedisOp;

    @Autowired
    private DefaultCacheService defaultCacheService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Test
    public void testObj() {
        stringRedisOp.set("test", "2020-07-16");
    }

    @Test
    public void test_def_method1() {
        System.out.println("第1次调用");
        Student student = defaultCacheService.method1("method1");
        System.out.println(student);

        System.out.println("第2次调用");
        student = defaultCacheService.method1("method1");
        System.out.println(student);

        System.out.println("第3次调用");
        student = defaultCacheService.method1("method1");
        System.out.println(student);
    }

    @Test
    public void test_def_method2() {
        Student st = new Student();
        st.setName("method2");
        st.setAge(20);

        System.out.println("第1次调用");
        Student student = defaultCacheService.method2(st, 30);
        System.out.println(student);

        System.out.println("第2次调用");
        student = defaultCacheService.method2(st, 30);
        System.out.println(student);

        System.out.println("第3次调用");
        student = defaultCacheService.method2(st, 30);
        System.out.println(student);
    }

    @Test
    public void test_def_method3() {
        Student st = new Student();
        st.setName("method3");
        st.setAge(20);

        System.out.println("第1次调用");
        Student student = defaultCacheService.method3(st);
        System.out.println(student);

        System.out.println("第2次调用");
        student = defaultCacheService.method3(st);
        System.out.println(student);

        System.out.println("第3次调用");
        student = defaultCacheService.method3(st);
        System.out.println(student);

        st.setAge(21);
        System.out.println("第4次调用");
        student = defaultCacheService.method3(st);
        System.out.println(student);

        System.out.println("第5次调用");
        student = defaultCacheService.method3(st);
        System.out.println(student);

        System.out.println("第6次调用");
        student = defaultCacheService.method3(st);
        System.out.println(student);
    }

    @Test
    public void test_def_method4() {
        Student st = new Student();
        st.setName("method4");
        st.setAge(20);

        System.out.println("第1次调用");
        Student student = defaultCacheService.method4(st);
        System.out.println(student);

        System.out.println("第2次调用");
        student = defaultCacheService.method4(st);
        System.out.println(student);

        System.out.println("第3次调用");
        student = defaultCacheService.method4(st);
        System.out.println(student);

        st.setAge(21);
        System.out.println("第4次调用");
        student = defaultCacheService.method4(st);
        System.out.println(student);

        System.out.println("第5次调用");
        student = defaultCacheService.method4(st);
        System.out.println(student);

        System.out.println("第6次调用");
        student = defaultCacheService.method4(st);
        System.out.println(student);
    }

    @Test
    public void test_redis_method1() {
        System.out.println("第1次调用");
        Student st = redisCacheService.getStudent("chenglei");
        System.out.println(st);

        System.out.println("第2次调用");
        st = redisCacheService.getStudent("chenglei");
        System.out.println(st);

        System.out.println("第3次调用");
        st = redisCacheService.getStudent("chenglei");
        System.out.println(st);

        System.out.println("第4次调用");
        st = redisCacheService.getStudent("chenglei");
        System.out.println(st);
    }

}
