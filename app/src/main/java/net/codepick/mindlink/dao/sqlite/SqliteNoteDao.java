package net.codepick.mindlink.dao.sqlite;

import net.codepick.mindlink.dao.NoteDao;
import net.codepick.mindlink.dao.exception.ObjectAlreadyExistException;
import net.codepick.mindlink.domain.Note;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqliteNoteDao implements NoteDao {

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    private SimpleJdbcInsert createNote;

    public SqliteNoteDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.createNote = new SimpleJdbcInsert(dataSource)
                .withTableName("notes")
                .usingColumns("themeId", "title", "content")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Note create(Note note) throws ObjectAlreadyExistException {
//        String createSql = "INSERT INTO 'notes'(themeId, title, content) VALUES(:themeId, :title, :content)";
        Note existingNote = getByTitleAndThemeId(note.getTitle(), note.getThemeId());
        if (existingNote != null) {
            throw new ObjectAlreadyExistException(existingNote);
        }

        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("themeId", note.getThemeId());
        fieldValues.put("title", note.getTitle());
        fieldValues.put("content", note.getContent());

        /**
         * Возможно использовать beanPropertySource
         */
//        SqlParameterSource fieldValues = new BeanPropertySqlParameterSource(note);

        long generatedId = createNote.executeAndReturnKey(fieldValues).longValue();
        note.setId(generatedId);
        return note;
    }

    @Override
    public Note getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM notes WHERE id = :id", new Object[]{id}, new NoteMapper());
    }

    @Override
    public List<Note> getByTheme(Long themeId) {
        return jdbcTemplate.query("SELECT * FROM notes WHERE themeId = :themeId", new Object[]{themeId}, new NoteMapper());
    }

    @Override
    public boolean update(Note note) {
        String sql = "UPDATE 'notes' SET themeId = :themeId, title = :title, content = :content WHERE id = :id";
        jdbcTemplate.update(sql, note.getThemeId(), note.getTitle(), note.getContent(), note.getId());
        return true; // TODO check fields
    }

    @Override
    public Note deleteById(Long id) {
        Note note = getById(id);
        if (note != null) {
            int retVal = jdbcTemplate.update("DELETE FROM 'notes' WHERE id = :id", id);
        }
        return note;
    }

    private Note getByTitleAndThemeId(String noteTitle, Long themeId) {
        String sql = "SELECT * FROM 'notes' WHERE title = :title AND themeId = :themeId";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{noteTitle, themeId}, new NoteMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static class NoteMapper implements RowMapper<Note> {

        @Override
        public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
            Note note = new Note();
            note.setThemeId(rs.getLong("themeId"));
            note.setId(rs.getLong("id"));
            note.setTitle(rs.getString("title"));
            note.setContent(rs.getString("content"));
            return note;
        }
    }
}
