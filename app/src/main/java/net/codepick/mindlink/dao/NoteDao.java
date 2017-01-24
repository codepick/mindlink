package net.codepick.mindlink.dao;

import net.codepick.mindlink.dao.exception.ObjectAlreadyExistException;
import net.codepick.mindlink.domain.Note;

import java.util.List;

public interface NoteDao {
    Note create(Note note) throws ObjectAlreadyExistException;

    Note getById(Long id);

    List<Note> getByTheme(Long themeId);

    boolean update(Note note);

    Note deleteById(Long id);
}
