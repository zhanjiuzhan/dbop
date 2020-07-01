package org.jpcl.dbop.service.impl;

import org.jpcl.dbop.dao.db1.NoteMapper;
import org.jpcl.dbop.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * @author Administrator
 */
@Service
public class NoteServiceImpl  {
    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);
    @Autowired
    private NoteMapper noteMapper;


    public List<Note> get() {
        return noteMapper.get();
    }

    @Transactional(rollbackFor=RuntimeException.class)
    public void add() {
        for (int i = 0; i < 10; i ++) {
            int t = new Random().nextInt(500);
            logger.info("note 添加: "+ t);
            Note note = new Note();
            note.setId(t);
            note.setName(t+ "名字");
            noteMapper.add(note);

            if (i == 2) {
                throw new RuntimeException();
            }
        }
    }
}
