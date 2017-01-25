CREATE TABLE IF NOT EXISTS 'themes' (
    'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    'parentId' INTEGER NOT NULL DEFAULT 0,
    'title' TEXT NOT NULL,
    CONSTRAINT unique_title UNIQUE('parentId', 'title')
);

CREATE TABLE IF NOT EXISTS 'notes' (
    'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    'themeId' INTEGER NOT NULL DEFAULT 0,
    'title' TEXT,
    'content' TEXT,
    CONSTRAINT unique_theme_title UNIQUE('themeId', 'title')
);

CREATE TABLE IF NOT EXISTS 'metadatass' (
    'version' TEXT
);