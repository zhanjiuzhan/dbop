package org.jpcl.dbop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
public class TestController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/get/{id}")
    public String test1(@PathVariable String id) {
        stringRedisTemplate.opsForValue().set("id", id);
        return stringRedisTemplate.opsForValue().get("id");
    }

    @GetMapping("/get1")
    public Test test2() {
        Test test = new Test();
        redisTemplate.opsForValue().set("test", test);
        redisTemplate.keys("");
        System.out.println(test);
        return (Test)redisTemplate.opsForValue().get("test");
    }

    @GetMapping("/get2")
    public Test1 test3() {
        Test1 test = new Test1();
        test.setTest2(new Test());
        test.setTests(new Test[]{new Test(), new Test()});
        redisTemplate.opsForList().leftPush("test1", test);
        System.out.println(test.toString());
        return (Test1)redisTemplate.opsForList().leftPop("test1");
    }

    @GetMapping("/get3")
    public Test2 test4() {
        Test2 test = new Test2();
        redisTemplate.opsForValue().set("test2", test);
        System.out.println(test.toString());
        return (Test2)redisTemplate.opsForValue().get("test2");
    }

    @GetMapping("/get4")
    public Test3 test5() {
        Test3 test = new Test3();
        redisTemplate.opsForValue().set("test3", test);
        System.out.println(test.toString());
        return (Test3)redisTemplate.opsForValue().get("test3");
    }
}
