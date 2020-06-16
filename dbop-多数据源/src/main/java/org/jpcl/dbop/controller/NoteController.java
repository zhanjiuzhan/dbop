package org.jpcl.dbop.controller;


import org.jpcl.dbop.model.Auth;
import org.jpcl.dbop.model.Note;
import org.jpcl.dbop.service.AuthService;
import org.jpcl.dbop.service.NoteService;
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
    private NoteService noteService;

    @Autowired
    private AuthService authService;

    @RequestMapping("/getNote")
    public List<Note> getNote() {
        return noteService.get();
    }

    @RequestMapping("/getAuth")
    public List<Auth> getAuth() {
        return authService.get();
    }

    @CrossOrigin
    @GetMapping("/cors")
    @ResponseBody
    public String test() {
        return "cors";
    }
}
