package org.jpcl.dbop.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jpcl.dbop.model.ClassModel;

import java.util.List;

@Mapper
public interface ClassMapper {
    @Select("SELECT * FROM class")
    List<ClassModel> get();
}
