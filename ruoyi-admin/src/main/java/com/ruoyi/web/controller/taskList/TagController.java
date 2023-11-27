package com.ruoyi.web.controller.taskList;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.taskList.domain.entity.Tag;
import com.ruoyi.taskList.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskList/tag")
public class TagController extends BaseController {

    @Autowired
    private ITagService tagService;

    @GetMapping("/list")
    public AjaxResult tagList() {
        List<Tag> list = tagService.selectTagList();

        return success(list);
    }

    @PostMapping
    public AjaxResult add(@Validated @RequestBody List<String> tagName) {
        if(!tagService.checkTagNameUnique(tagName)) {
            return error("标签有重复的");
        }
        return toAjax(tagService.insertTag(tagName));
    }
}
