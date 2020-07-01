package org.jpcl.dbop.service;

import org.jpcl.dbop.model.Student;

import java.util.List;

/**
 * @author Administrator
 */
public interface StudentService {
    /**
     * 取得所有的学生信息
     * @return
     */
    List<Student> gets();

    /**
     * 修改年龄
     * @return
     */
    void updateAge();

    /**
     * 根据id取得该对象
     * @param id
     * @return
     */
    Student getById(String id);

    void a() throws Exception;

    void b() throws Exception;
}
