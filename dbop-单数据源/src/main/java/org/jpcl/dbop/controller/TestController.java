package org.jpcl.dbop.controller;


import org.jpcl.dbop.model.ClassModel;
import org.jpcl.dbop.model.User;
import org.jpcl.dbop.service.ClassModelService;
import org.jpcl.dbop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassModelService classModelService;

    @RequestMapping("/getUser")
    public List<User> getUser() {
        return userService.get();
    }

    @RequestMapping("/getClass")
    public List<ClassModel> getClassModel() {
        return classModelService.get();
    }
}
