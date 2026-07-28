package com.aliabullah.dailygoaltracker;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailySummaryScheduler {

    private final AdminController adminController;

    public DailySummaryScheduler(AdminController adminController) {
        this.adminController = adminController;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void run() {
        adminController.generateDailySummary();
    }
}