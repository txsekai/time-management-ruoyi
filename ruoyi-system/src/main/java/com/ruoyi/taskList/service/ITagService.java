package com.ruoyi.taskList.service;

import com.ruoyi.taskList.domain.entity.Tag;

import java.util.List;

public interface ITagService {
    public List<Tag> selectTagList();

    public boolean checkTagNameUnique(List<String> tagName);

    public int insertTag(List<String> tagName);
}