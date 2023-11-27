package com.ruoyi.taskList.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.taskList.mapper.TagMapper;
import com.ruoyi.taskList.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.taskList.domain.entity.Tag;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements ITagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> selectTagList() { return tagMapper.selectTagList(); }

    @Override
    public boolean checkTagNameUnique(List<String> tagName) {
        Tag info = tagMapper.checkTagNameUnique(tagName);

        if(info != null) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public int insertTag(List<String> tagName) {
        int rows = tagMapper.insertTag(tagName);

        return rows;
    }
}
