package org.jpcl.dbop.service.impl;

import org.jpcl.dbop.dao.mapper.ClassMapper;
import org.jpcl.dbop.model.ClassModel;
import org.jpcl.dbop.service.ClassModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassModelServiceImpl implements ClassModelService {

    @Autowired
    private ClassMapper classMapper;

    @Override
    public List<ClassModel> get() {
        return classMapper.get();
    }
}
