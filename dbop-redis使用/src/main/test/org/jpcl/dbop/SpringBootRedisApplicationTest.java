package org.jpcl.dbop;

import org.jpcl.dbop.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRedisApplicationTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testObj() throws Exception {
        Student student = new Student();
        student.setAge(28);
        student.setName("SP神*赵云");
        student.setDes(new String[]{"三国杀", "王者荣耀", "剑网3指尖江湖"});
        student.setPhone(12345678987l);
        redisTemplate.opsForValue().set(student.getName(), student);
        System.out.println("redis 存储成功");
        System.out.println("存储的信息为: " + redisTemplate.opsForValue().get(student.getName()));
    }
}
