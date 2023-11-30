package com.ruoyi.taskList.service;

import com.ruoyi.taskList.domain.entity.Tag;
import com.ruoyi.taskList.domain.entity.TaskTags;
import com.ruoyi.taskList.domain.query.TaskTagsParam;

import java.util.List;

public interface ITagService {
    public List<Tag> selectTagList();

    public boolean checkTagNameUnique(List<Tag> tag);

    public int insertTag(List<Tag> tag);

    public int deleteTagById(Long tagId);

    public List<TaskTags> taskTagsList(Long taskId, List<Long> tagIds);

    public int insertTagToTask(TaskTagsParam taskTagsParam);

    public int delTagToTask(TaskTagsParam taskTagsParam);
}
