package net.codepick.mindlink.dao.sqlite;

import net.codepick.mindlink.dao.ThemeDao;
import net.codepick.mindlink.dao.exception.ObjectNotExistException;
import net.codepick.mindlink.domain.Theme;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.codepick.mindlink.utils.IOUtils.message;

public class SqliteThemeDao implements ThemeDao {

    private JdbcTemplate jdbcTemplate;
    private CreateThemeQuery createThemeQuery;
    private DataSource dataSource;

    public SqliteThemeDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.createThemeQuery = new CreateThemeQuery(dataSource);
    }

    @Override
    public Theme create(Theme theme) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", theme.getParentId() == null ? 0 : theme.getParentId());
        params.put("title", theme.getTitle());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        createThemeQuery.updateByNamedParam(params, keyHolder);
        theme.setId(keyHolder.getKey().longValue());
        return theme;
    }

    @Override
    public Theme getById(Long id) {
        String sql = "SELECT * FROM themes WHERE id = :id";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new ThemeMapper());
    }

    @Override
    public Theme getByNamePath(String... titles) throws ObjectNotExistException {
        Long nextParentId = ROOT_THEME_ID;
        Theme theme = null;
        for (String nextTitle : titles) {
            theme = getByTitleAndParentId(nextTitle, nextParentId);
            if (theme == null) {
                throw new ObjectNotExistException(nextTitle);
            }
            nextParentId = theme.getId();
        }
        return theme;
    }

    @Override
    public boolean update(Theme theme) {
        if (theme.getId() == null) {
            message("Can not update " + theme + ", id is null");
            return false;
        }
        if (getById(theme.getId()) == null) {
            message("Can not update " + theme + ", not found");
            return false;
        }
        String sql = "UPDATE themes SET parentId = :parentId, title = :title WHERE id = :id";
        jdbcTemplate.update(sql, theme.getParentId(), theme.getTitle(), theme.getId());
        return true;
    }

    @Override
    public Theme deleteById(Long id) {
        Theme theme = getById(id);
        if (theme != null) {
            String sql = "DELETE FROM themes WHERE id = :id";
            jdbcTemplate.update(sql, id);
        } else {
            message("Can not delete theme with id = " + id + ", not found");
        }
        return theme;
    }

    @Override
    public List<Theme> getRootThemes() {
        String sql = "SELECT * FROM themes WHERE parentId = :rootId";
        return jdbcTemplate.query(sql, new ThemeMapper(), ROOT_THEME_ID);
    }

    @Override
    public List<Theme> getChildrenById(Long parentId) {
        String sql = "SELECT * FROM themes WHERE parentId = :parentId";
        return jdbcTemplate.query(sql, new ThemeMapper(), parentId);
    }

    @Override
    public Theme createByNamePath(String... titles) {
        Long nextParentId = ROOT_THEME_ID;
        Theme theme = null;
        for (String nextTitle : titles) {
            theme = getByTitleAndParentId(nextTitle, nextParentId);
            if (theme == null) {
                theme = new Theme();
                theme.setParentId(nextParentId);
                theme.setTitle(nextTitle);
                create(theme);
            }
            nextParentId = theme.getId();
        }
        return theme;
    }

    private Theme getByTitleAndParentId(String themeTitle, Long parentThemeId) {
        String sql = "SELECT * FROM 'themes' WHERE title = :title AND parentId = :parentId";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{themeTitle, parentThemeId}, new ThemeMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static class CreateThemeQuery extends SqlUpdate {
        private static final String sqlStatement = "INSERT INTO 'themes' (parentId, title) VALUES (:parentId, :title)";

        public CreateThemeQuery(DataSource dataSource) {
            super(dataSource, sqlStatement);
            declareParameter(new SqlParameter("parentId", Types.INTEGER));
            declareParameter(new SqlParameter("title", Types.VARCHAR));
            setGeneratedKeysColumnNames("id");
            setReturnGeneratedKeys(true);
        }
    }

    private static class ThemeMapper implements RowMapper<Theme> {
        @Override
        public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
            Theme theme = new Theme();
            theme.setId(rs.getLong("id"));
            theme.setParentId(rs.getLong("parentId"));
            theme.setTitle(rs.getString("title"));
            return theme;
        }
    }
}
