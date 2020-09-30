package org.jpcl.dbop.dao.db1;

import org.apache.ibatis.annotations.Select;
import org.jpcl.dbop.model.Note;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author Administrator
 */
public interface NoteMapper {

    @Select("select * from note")
    List<Note> get();
}
