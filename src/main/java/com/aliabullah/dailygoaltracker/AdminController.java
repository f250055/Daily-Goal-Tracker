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

        summary.append("<html><body style='font-family: Arial, sans-serif;'>");
        summary.append("<h2 style='color:#00695C;'>Daily Goal Summary</h2>");
        summary.append("<p style='color:#555;'>").append("Dated: "+summaryDate).append("</p>");

        if (tasks.isEmpty()) {
            summary.append("<p>No tasks for this day.</p>");
        } else {
            summary.append("<table style='width:100%; border-collapse: collapse;'>");
            for (Task task : tasks) {
                String statusIcon = task.isCompleted() ? "✅" : "❌";
                String priorityColor = "MEDIUM".equals(String.valueOf(task.getPriority())) ? "#FFA000"
                        : "HIGH".equals(String.valueOf(task.getPriority())) ? "#D32F2F" : "#2E7D32";

                summary.append("<tr style='border-bottom: 1px solid #eee;'>");
                summary.append("<td style='padding:8px; font-size:16px;'>").append(statusIcon).append("</td>");
                summary.append("<td style='padding:8px;'>");
                summary.append("<strong>").append(task.getDescription()).append("</strong><br/>");
                summary.append("<span style='color:").append(priorityColor).append(";'>")
                        .append(task.getPriority()).append("</span>");
                summary.append(" &middot; ");
                summary.append("<span style='color:#777;'>").append(task.getCategory()).append("</span>");
                summary.append("</td></tr>");
            }
            summary.append("</table>");
        }

        summary.append("</body></html>");

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