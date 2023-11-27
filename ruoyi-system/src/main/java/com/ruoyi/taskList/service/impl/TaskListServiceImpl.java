package com.ruoyi.taskList.service.impl;

import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import com.ruoyi.taskList.mapper.TaskListMapper;
import com.ruoyi.taskList.service.ITaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskListServiceImpl implements ITaskListService {

    @Autowired
    private TaskListMapper taskListMapper;


    @Override
    public List<TaskList> selectTodoList(TaskListQueryParam taskListQueryParam) { return taskListMapper.selectTodoList(taskListQueryParam); }
}
