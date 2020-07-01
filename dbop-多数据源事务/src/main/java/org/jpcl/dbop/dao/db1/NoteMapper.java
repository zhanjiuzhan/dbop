package org.jpcl.dbop.dao.db1;

import org.apache.ibatis.annotations.Insert;
import org.jpcl.dbop.model.Note;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author Administrator
 */
@Qualifier("db1SqlSessionTemplate")
public interface NoteMapper {

    @Select("select * from note")
    List<Note> get();

    @Insert("insert into note(id, name) values (#{id}, #{name})")
    boolean add(Note note);
}
