package org.jpcl.dbop.service.impl;

import org.jpcl.dbop.dao.mapper.StudentMapper;
import org.jpcl.dbop.model.Student;
import org.jpcl.dbop.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 */
@Service
public class StudentService2 {
    private static final Logger logger = LoggerFactory.getLogger(StudentService2.class);

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentService studentService;

    @Transactional(rollbackFor=Exception.class)
    public void a() throws Exception {
        try {
            studentService.b();
        } catch (Exception e) {

        } finally {
            for (int i = 0; i < 11; i++) {
                logger.info("修改年龄成为了: " + i + " 未提交");
                Student st = new Student();
                st.setId("2");
                st.setAge(i);
                studentMapper.update(st);
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    throw new RuntimeException();
                }

            }
            logger.info("修改年龄完成!");
        }

    }
}
