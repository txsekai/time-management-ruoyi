package com.ruoyi.taskList.mapper;

import com.ruoyi.taskList.domain.entity.Tag;
import com.ruoyi.taskList.domain.entity.TaskTags;
import com.ruoyi.taskList.domain.query.TaskTagsParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {
    public List<Tag> selectTagList();

    public List<Tag> checkTagNameUnique(List<String> tagName);

    public int insertTag(List<Tag> tag);

    public int deleteTagById(Long tagId);

    public List<TaskTags> taskTagsList(Long taskId, List<Long> tagIds);

//    public int insertTagToTask(@Param("taskId") Long taskId, @Param("tagIds") List<Long> tagIds);
    public int insertTagToTask(TaskTagsParam taskTagsParam);

//    public int delTagToTask(Long taskId, List<Long> tagIds);
    public int delTagToTask(TaskTagsParam taskTagsParam);
}
