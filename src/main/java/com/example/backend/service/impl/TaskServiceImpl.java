package com.example.backend.service.impl;

import com.example.backend.dto.task.request.*;
import com.example.backend.dto.task.response.*;
import com.example.backend.repository.TaskRepository;
import com.example.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskListResponse getAllTasks(TaskFilterRequest filter) {
        return null; // Tạm thời để null để hết lỗi
    }

    @Override
    public TaskDetailResponse createTask(CreateTaskRequest request) {
        return null;
    }

    @Override
    public void updateTask(Long id, UpdateTaskRequest request) {
        // Trống tạm thời
    }

    @Override
    public void assignTask(AssignTaskRequest request) {
        // Trống tạm thời
    }
}