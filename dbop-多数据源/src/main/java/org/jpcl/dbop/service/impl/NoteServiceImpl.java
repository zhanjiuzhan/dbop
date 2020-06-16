package org.jpcl.dbop.service.impl;

import org.jpcl.dbop.dao.db1.NoteMapper;
import org.jpcl.dbop.model.Note;
import org.jpcl.dbop.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public List<Note> get() {
        return noteMapper.get();
    }
}
