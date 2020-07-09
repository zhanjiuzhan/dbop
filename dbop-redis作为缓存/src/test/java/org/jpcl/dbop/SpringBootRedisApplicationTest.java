package org.jpcl.dbop;

import dbop.WebApplication;
import dbop.config.StringRedisOp;
import dbop.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= WebApplication.class)
public class SpringBootRedisApplicationTest {

    @Autowired
    @Qualifier("stringRedisOp1")
    private StringRedisOp stringRedisOp;

    @Test
    public void testObj() {
        Student student = new Student();
        student.setAge(28);
        student.setName("SP神*赵云");
        student.setPhone(12345678987l);
        stringRedisOp.set(student.getName(), student);
        System.out.println("redis 存储成功");
        System.out.println("存储的信息为: " + stringRedisOp.get(student.getName()));
    }
}
