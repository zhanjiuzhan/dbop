package org.jpcl.dbop.dao.db2;

import org.apache.ibatis.annotations.Insert;
import org.jpcl.dbop.model.Student;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author Administrator
 */
@Qualifier("db2SqlSessionTemplate")
public interface StudentMapper {

    @Select("select * from student")
    List<Student> get();

    @Insert("insert into student values (#{id}, #{name}, #{age})")
    boolean add(Student student);
}
