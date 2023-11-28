package com.ruoyi.taskList.mapper;

import com.ruoyi.taskList.domain.entity.Tag;

import java.util.List;

public interface TagMapper {
    public List<Tag> selectTagList();

    public List<Tag> checkTagNameUnique(List<String> tagName);

    public int insertTag(List<Tag> tag);
}
