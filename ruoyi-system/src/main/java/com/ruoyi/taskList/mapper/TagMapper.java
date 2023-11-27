package com.ruoyi.taskList.mapper;

import com.ruoyi.taskList.domain.entity.Tag;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TagMapper {
    public List<Tag> selectTagList();

    public Tag checkTagNameUnique(@Param("tagName") List<String> tagName);

    public int insertTag(List<String> tagName);
}
