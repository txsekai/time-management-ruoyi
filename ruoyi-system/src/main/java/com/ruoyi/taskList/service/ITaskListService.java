package com.ruoyi.taskList.service;

import com.ruoyi.taskList.domain.entity.TaskList;

import java.util.List;

public interface ITaskListService {
    public List<TaskList> selectTodoList(TaskList taskList);
}
