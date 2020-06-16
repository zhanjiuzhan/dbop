package org.jpcl.dbop.service.impl;

import org.jpcl.dbop.dao.db2.AuthMapper;
import org.jpcl.dbop.model.Auth;
import org.jpcl.dbop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> get() {
        return authMapper.get();
    }
}
