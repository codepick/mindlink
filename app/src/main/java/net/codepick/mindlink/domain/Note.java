package net.codepick.mindlink.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Note {
    private Long id;
    private Long themeId;
    private String title;
    private String content;
    private String sourceFrom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("themeId", themeId)
                .append("title", title)
                .append("content", content)
                .append("sourceFrom", sourceFrom)
                .toString();
    }
}
