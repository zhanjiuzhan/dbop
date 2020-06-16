package org.jpcl.dbop.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jpcl.dbop.model.User;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> get();
}
