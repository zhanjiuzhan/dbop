package org.jpcl.dbop.service.impl;

import org.jpcl.dbop.dao.mapper.StudentMapper;
import org.jpcl.dbop.model.Student;
import org.jpcl.dbop.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * @author Administrator
 */
@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> gets() {
        return studentMapper.gets();
    }

    @Transactional(isolation= Isolation.SERIALIZABLE, rollbackFor=Exception.class)
    @Override
    public void updateAge() {
        for (int i = 1; i < 11; i++) {
            logger.info("修改年龄成为了: " + i + " 未提交");
            Student st = new Student();
            st.setId("1");
            st.setAge(i);
            studentMapper.update(st);
            try {
                Thread.sleep(1000);
            } catch (Exception e){
                throw new RuntimeException();
            }
        }
        logger.info("修改年龄完成!");
    }

    @Transactional(isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    @Override
    public Student getById(String id) {
        logger.info("取得信息, id: " + id);
        int ran = new Random().nextInt(10);
        logger.info("准备读取, 需要休息: " + ran + " 秒");
        try {
            Thread.sleep(ran * 1000);
        } catch (Exception e){}
        return studentMapper.getById(id);
    }


    @Override
    public void a() throws Exception {
        logger.info("修改年龄完成!");
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor=Exception.class)
    @Override
    public void b() throws Exception {
        for (int i = 20; i < 31; i++) {
            logger.info("修改年龄成为了: " + i + " 未提交");
            Student st = new Student();
            st.setId("1");
            st.setAge(i);
            studentMapper.update(st);
            try {
                Thread.sleep(500);
            } catch (Exception e){
                throw new RuntimeException();
            }

        }
        logger.info("修改年龄完成!");
    }

}
