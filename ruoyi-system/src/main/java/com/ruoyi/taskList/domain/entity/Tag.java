package com.ruoyi.taskList.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Tag extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long tagId;

    private String tagName;

    public Long getTagId() { return tagId; }

    public void setTagId(Long tagId) { this.tagId = tagId; }

    public String getTagName() { return tagName; }

    public void setTagName(String tagName) { this.tagName = tagName; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("tagId", getTagId())
                .append("tagName", getTagName())
                .toString();
    }
}
