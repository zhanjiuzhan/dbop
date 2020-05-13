package org.jpcl.dbop.demo.controller;

import org.jpcl.dbop.dao.db1.NoteMapper;
import org.jpcl.dbop.dao.db2.AuthMapper;
import org.jpcl.dbop.model.Auth;
import org.jpcl.dbop.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private AuthMapper authMapper;

    @RequestMapping("/getNote")
    public List<Note> getNote() {
        return noteMapper.get();
    }

    @RequestMapping("/getAuth")
    public List<Auth> getAuth() {
        return authMapper.get();
    }

    @CrossOrigin
    @GetMapping("/cors")
    @ResponseBody
    public String test() {
        return "cors";
    }
}
