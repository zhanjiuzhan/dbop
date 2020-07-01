package org.jpcl.dbop.service.impl;

import org.jpcl.dbop.dao.db2.StudentMapper;
import org.jpcl.dbop.model.Note;
import org.jpcl.dbop.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * @author Administrator
 */
@Service
public class StudentServiceImpl  {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    @Autowired
    private StudentMapper studentMapper;


    public List<Student> get() {
        return studentMapper.get();
    }

    @Transactional(rollbackFor=RuntimeException.class)
    public void add() {
        for (int i = 0; i < 10; i ++) {
            int t = new Random().nextInt(500);
            logger.info("student 添加: "+ t);
            Student st = new Student();
            st.setId(t + "");
            st.setName(t+ "名字");
            st.setAge(t);
            studentMapper.add(st);

            if (i == 2) {
                throw new RuntimeException();
            }
        }
    }
}
