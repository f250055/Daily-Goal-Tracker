package com.aliabullah.dailygoaltracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("github")
public class DailySummaryRunner implements CommandLineRunner {

    private final AdminController adminController;

    public DailySummaryRunner(AdminController adminController) {
        this.adminController = adminController;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== DailySummaryRunner is running ===");
        adminController.generateDailySummary();
        System.exit(0);
    }
}