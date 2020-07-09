package org.jpcl.dbop;

import org.jpcl.dbop.config.StringRedisOp;
import org.jpcl.dbop.model.Hobby;
import org.jpcl.dbop.model.Parent;
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

        stringRedisOp.set(student.getName(), student);
        System.out.println("redis 存储成功");
        System.out.println("存储的信息为: " + stringRedisOp.get(student.getName()));
    }
}
