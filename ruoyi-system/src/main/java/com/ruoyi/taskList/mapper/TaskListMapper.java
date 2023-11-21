package com.ruoyi.taskList.mapper;

import com.ruoyi.taskList.domain.entity.TaskList;

import java.util.List;

public interface TaskListMapper {
    public List<TaskList> selectTodoList(TaskList list);
}
