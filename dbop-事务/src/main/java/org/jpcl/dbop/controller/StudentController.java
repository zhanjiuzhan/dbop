package org.jpcl.dbop.controller;

import org.jpcl.dbop.model.Student;
import org.jpcl.dbop.service.StudentService;
import org.jpcl.dbop.service.impl.StudentService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentService2 studentService2;

    @GetMapping("/gets")
    public List<Student> gets() {
        return studentService.gets();
    }

    /**
     * isolation测试
     * read_uncommitted
     * read_committed
     * repeatable read
     * serializable
     */
    @GetMapping("/read_uncommitted")
    public void batchAdd() {
        new Thread(() -> studentService.updateAge()).start();
        new Thread(() -> System.out.println(studentService.getById("1"))).start();
    }

    @GetMapping("/propagation")
    public void propagation() throws Exception {
        studentService2.a();
    }
}
