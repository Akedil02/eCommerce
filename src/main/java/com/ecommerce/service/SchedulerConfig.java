package com.ecommerce.service;

import com.ecommerce.service.BackgroundTaskService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@EnableAsync
public class SchedulerConfig {

    private final BackgroundTaskService taskService;

    public SchedulerConfig(BackgroundTaskService taskService) {
        this.taskService = taskService;
    }

    // every 5 second
    @Scheduled(fixedRate = 5000)
    public void runBackgroundTasks() {
        taskService.processTasks();
    }
}
