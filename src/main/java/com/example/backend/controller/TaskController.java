package com.example.backend.controller;

import com.example.backend.dto.task.request.*;
import com.example.backend.dto.task.response.*;
import com.example.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks") // Đường dẫn gốc của API
public class TaskController {

    @Autowired
    private TaskService taskService;

    // 1. Lấy danh sách Task
    @GetMapping
    public ResponseEntity<TaskListResponse> getAllTasks(TaskFilterRequest filter) {
        return ResponseEntity.ok(taskService.getAllTasks(filter));
    }

    // 2. Tạo mới Task
    @PostMapping
    public ResponseEntity<TaskDetailResponse> createTask(@RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    // 3. Cập nhật Task
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        taskService.updateTask(id, request);
        return ResponseEntity.noContent().build();
    }

    // 4. Giao Task cho nhân viên
    @PostMapping("/assign")
    @PatchMapping("/assign")
    public ResponseEntity<Void> assignTask(@RequestBody AssignTaskRequest request) {
        taskService.assignTask(request);
        return ResponseEntity.ok().build();
    }
}
