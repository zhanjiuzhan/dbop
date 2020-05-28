package org.jpcl.dbop.dao.mysql.db2;

import org.apache.ibatis.annotations.Select;
import org.jpcl.dbop.model.Auth;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author Administrator
 */
@Qualifier("db2SqlSessionTemplate")
public interface AuthMapper {

    @Select("select * from auth")
    List<Auth> get();
}
