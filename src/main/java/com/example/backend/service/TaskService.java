package com.example.backend.service;
import com.example.backend.dto.task.request.*;
import com.example.backend.dto.task.response.*;
public interface TaskService {
    // Lấy danh sách task (Dùng TaskFilterRequest và trả về TaskListResponse)
    TaskListResponse getAllTasks(TaskFilterRequest filter);

    // Tạo mới task (Dùng CreateTaskRequest và trả về TaskDetailResponse)
    TaskDetailResponse createTask(CreateTaskRequest request);

    // Cập nhật task (Dùng UpdateTaskRequest)
    void updateTask(Long id, UpdateTaskRequest request);

    // Giao task cho ai đó (Dùng AssignTaskRequest)
    void assignTask(AssignTaskRequest request);
}