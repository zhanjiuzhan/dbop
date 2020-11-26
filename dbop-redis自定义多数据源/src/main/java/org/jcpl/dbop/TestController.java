package org.jcpl.dbop;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
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
