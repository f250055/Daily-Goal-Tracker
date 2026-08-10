package com.aliabullah.dailygoaltracker;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final ZoneId PAKISTAN_ZONE = ZoneId.of("Asia/Karachi");
    private final EmailService emailService;
    private final TaskRepository taskRepository;

    public AdminController(EmailService emailService, TaskRepository taskRepository) {
        this.emailService = emailService;
        this.taskRepository = taskRepository;
    }
    public void generateDailySummary() {
        LocalDate summaryDate = LocalDate.now(PAKISTAN_ZONE).minusDays(1);
        List<Task> tasks = taskRepository.findByTaskDate(String.valueOf(summaryDate));
        StringBuilder summary = new StringBuilder();

        if (tasks.isEmpty()) {
            summary.append("No tasks for ").append(summaryDate).append(".");
        } else {
            summary.append("Progress for ").append(summaryDate).append(":\n");
            for (Task task : tasks) {
                if (task.isCompleted()) {
                    summary.append("✅ ");
                } else {
                    summary.append("❌ ");
                }
                summary.append(task.getDescription());
                summary.append("\nCategory: ");
                summary.append(task.getCategory());
                summary.append("\nPriority: ");
                summary.append(task.getPriority());
                summary.append("\n");
            }
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