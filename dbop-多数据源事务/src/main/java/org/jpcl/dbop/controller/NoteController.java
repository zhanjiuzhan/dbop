package org.jpcl.dbop.controller;

import org.jpcl.dbop.model.Note;
import org.jpcl.dbop.model.Student;
import org.jpcl.dbop.service.impl.NoteServiceImpl;
import org.jpcl.dbop.service.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
public class NoteController {

    @Autowired
    private NoteServiceImpl noteService;

    @Autowired
    private StudentServiceImpl studentService;

    @RequestMapping("/getNote")
    public List<Note> getNote() {
        return noteService.get();
    }

    @RequestMapping("/getStudent")
    public List<Student> getAuth() {
        return studentService.get();
    }

    @GetMapping("/test1")
    public String test() {
        noteService.add();
        return "一个事务包含一种数据源db1";
    }

    @GetMapping("/test2")
    public String test2() {
        studentService.add();
        return "一个事务包含一种数据源db1";
    }
}
