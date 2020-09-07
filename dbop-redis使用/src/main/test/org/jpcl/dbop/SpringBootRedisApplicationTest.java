package org.jpcl.dbop;

import org.jpcl.dbop.config.DefineRedis;
import org.jpcl.dbop.model.Hobby;
import org.jpcl.dbop.model.Notice;
import org.jpcl.dbop.model.Parent;
import org.jpcl.dbop.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRedisApplicationTest {

    @Autowired
    private DefineRedis redis;

    @Test
    public void testObj() {
        Student student = new Student();
        student.setAge(28);
        student.setName("SP神*赵云");
        student.setDes(new String[]{"三国杀", "王者荣耀", "剑网3指尖江湖"});
        student.setPhone(12345678987l);
        Hobby hobby = new Hobby();
        hobby.setDes("我爱吃辣的");
        student.setHobby(hobby);
        Parent parent = new Parent();
        parent.setName("haha");
        Parent p = new Parent();
        p.setName("heihei");
        student.setParents(new ArrayList<Parent>(){
            {
                add(parent);
                add(p);
            }
        });
        student.setMap(new HashMap<String, Hobby>(2){
            {
                put("1", new Hobby());
                put("2", hobby);
            }
        });

        redis.set(student.getName(), student);
        System.out.println("redis 存储成功");
        System.out.println("存储的信息为: " + redis.get(student.getName()));
    }

    @Test
    public void test2() {
        final String NOTICE_KEY = "customer_system_notice_content";
        Notice notice = new Notice();
        notice.setContent("测试1");
        notice.setTime(60);
        notice.setTitle("测试title");
        notice.setType(1);
        redis.set(NOTICE_KEY, notice);
        System.out.println(redis.get(NOTICE_KEY).toString());
    }
}
