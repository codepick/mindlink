package net.codepick.mindlink.dao;

import net.codepick.mindlink.dao.exception.ObjectNotExistException;
import net.codepick.mindlink.domain.Theme;

import java.util.List;

public interface ThemeDao {
    Long ROOT_THEME_ID = 0L;

    Theme create(Theme theme);

    Theme createByNamePath(String... titles);

    Theme getById(Long id);

    Theme getByNamePath(String... titles) throws ObjectNotExistException;

    boolean update(Theme theme);

    Theme deleteById(Long id);

    List<Theme> getRootThemes();

    List<Theme> getChildrenById(Long parentId);
}
