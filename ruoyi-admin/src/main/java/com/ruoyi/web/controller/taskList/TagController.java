package com.ruoyi.web.controller.taskList;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.taskList.domain.entity.Tag;
import com.ruoyi.taskList.domain.entity.TaskTags;
import com.ruoyi.taskList.domain.query.TaskTagsParam;
import com.ruoyi.taskList.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping(value="addTag")
    public AjaxResult add(@Validated @RequestBody List<Tag> tag) {
        if(!tagService.checkTagNameUnique(tag)) {
            return error("标签有重复的");
        }
        return toAjax(tagService.insertTag(tag));
    }

    @DeleteMapping("/{tagId}")
    public AjaxResult removeTagId(@PathVariable Long tagId) {
        return toAjax(tagService.deleteTagById(tagId));
    }

    @GetMapping("/listTaskTags")
    public AjaxResult taskTagsList(@RequestParam("taskId") Long taskId, @RequestParam("tagIds") List<Long> tagIds) {

        List<TaskTags> taskTagsList = tagService.taskTagsList(taskId, tagIds);

        return success(taskTagsList);
    }

    @PostMapping("/selectTagToTask")
    public AjaxResult addTagToTask(@RequestBody TaskTagsParam taskTagsParam) {

        return toAjax(tagService.insertTagToTask(taskTagsParam));
    }

    @DeleteMapping("deselectTagToTask")
    public AjaxResult removeTagToTask(@RequestBody TaskTagsParam taskTagsParam) {

        return toAjax(tagService.delTagToTask(taskTagsParam));
    }
}
