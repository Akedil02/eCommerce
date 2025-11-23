package com.ecommerce.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BackgroundTaskService {

    private final RedisTemplate<String, String> redisTemplate;

    public BackgroundTaskService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void enqueueTask(String task) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        ops.rightPush("taskQueue", task);
    }


    @Async
    public void processTasks() {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        String task;
        while ((task = ops.leftPop("taskQueue", 1, TimeUnit.SECONDS)) != null) {
            System.out.println("Processing task: " + task);

            if (task.startsWith("EMAIL:")) {
                sendEmail(task.substring(6));
            } else if (task.startsWith("REPORT:")) {
                generateReport(task.substring(7));
            } else if (task.startsWith("FILE:")) {
                processFile(task.substring(5));
            }
        }
    }

    private void sendEmail(String emailData) {
        System.out.println("Sending email: " + emailData);
    }

    private void generateReport(String reportData) {
        System.out.println("Generating report: " + reportData);
    }

    private void processFile(String fileName) {
        System.out.println("Processing file: " + fileName);
    }
}
