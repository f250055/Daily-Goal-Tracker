package com.aliabullah.dailygoaltracker;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final EmailService emailService;
    private final TaskRepository taskRepository;

    public AdminController(EmailService emailService, TaskRepository taskRepository) {
        this.emailService = emailService;
        this.taskRepository = taskRepository;
    }
    public void generateDailySummary() {
        List<Task> tasks = taskRepository.findAll();
        StringBuilder summary = new StringBuilder();
        summary.append("The today progress list is : \n");

        for (Task task : tasks) {
            if (task.isCompleted()) {
                summary.append("✅ ");
            } else {
                summary.append("❌ ");
            }
            summary.append(task.getDescription());
            summary.append("\n");
        }
        emailService.sendSummaryEmail(summary.toString());
    }
    @Value("${ADMIN_API_KEY}")
    private String adminApiKey;

    @PostMapping("/generate-summary")
    public ResponseEntity<String> triggerSummary(@RequestHeader("X-API-Key") String key) {
        if (!key.equals(adminApiKey)) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        generateDailySummary();
        return ResponseEntity.ok("Summary sent!");
    }
}