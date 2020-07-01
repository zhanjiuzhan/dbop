package org.jpcl.dbop.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jpcl.dbop.model.Student;

import java.util.List;

@Mapper
public interface StudentMapper {

    /**
     * 取得所有的学生信息
     * @return
     */
    @Select("SELECT * FROM student")
    List<Student> gets();

    @Insert("insert into student values (#{id}, #{name}, #{age})")
    int add(Student student);

    @Update("update student set age = #{age} where id = #{id}")
    int update(Student student);

    @Select("SELECT * FROM student WHERE id = #{id} limit 1")
    Student getById(String id);
}
