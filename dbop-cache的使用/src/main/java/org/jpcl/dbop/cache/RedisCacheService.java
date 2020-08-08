package org.jpcl.dbop.cache;

import org.jpcl.dbop.model.Student;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class RedisCacheService {

    @Cacheable("redisCache")
    public Student getStudent(String name) {
        System.out.println("redisCache 方法执行...");
        Student st = new Student();
        st.setName(name);
        st.setAge(28);
        st.setPhone(13002925126L);
        return st;
    }
}
