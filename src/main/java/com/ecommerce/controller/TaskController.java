package com.ecommerce.controller;

import com.ecommerce.service.BackgroundTaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final BackgroundTaskService taskService;

    public TaskController(BackgroundTaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/enqueue")
    public String enqueueTask(@RequestParam String type, @RequestParam String payload) {
        String task = type.toUpperCase() + ":" + payload;
        taskService.enqueueTask(task);
        return "Task queued: " + task;
    }
}
