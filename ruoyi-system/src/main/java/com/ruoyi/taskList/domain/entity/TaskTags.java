package com.ruoyi.taskList.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TaskTags extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long taskId;

    private Long tagId;

    private Tag tagData;

    public Long getTaskId() { return taskId; }

    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public Long getTagId() { return tagId; }

    public void setTagId(Long tagId) {this.tagId = tagId;}

    public Tag getTagData() { return tagData; }

    public void setTagData(Tag tagData) { this.tagData = tagData; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskId", getTaskId())
                .append("tagId", getTagId())
                .append("tagName", getTagData())
                .toString();
    }
}
