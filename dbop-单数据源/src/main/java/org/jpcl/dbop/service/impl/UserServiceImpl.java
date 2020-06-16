package org.jpcl.dbop.service.impl;

import org.jpcl.dbop.dao.mapper.UserMapper;
import org.jpcl.dbop.model.User;
import org.jpcl.dbop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> get() {
        return userMapper.get();
    }
}
