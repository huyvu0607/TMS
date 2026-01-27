package com.example.backend.service;

import com.example.backend.dto.task.request.*;
import com.example.backend.dto.task.response.*;

public interface ITaskService {
    // 1. Lấy danh sách task (kèm lọc)
    TaskListResponse getAllTasks(TaskFilterRequest filter);

    // 2. Tạo mới task
    TaskDetailResponse createTask(CreateTaskRequest request);

    // 3. Cập nhật task theo ID
    void updateTask(Long id, UpdateTaskRequest request);

    // 4. Giao task cho nhân viên
    void assignTask(AssignTaskRequest request);
}