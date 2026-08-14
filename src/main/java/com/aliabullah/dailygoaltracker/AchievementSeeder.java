package com.aliabullah.dailygoaltracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AchievementSeeder implements CommandLineRunner {

    private final AchievementRepository achievementRepository;

    public AchievementSeeder(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }
    @Override
    public void run(String... args) {

        if (achievementRepository.count() > 0) {
            return;
        }

        // Milestones
        achievementRepository.save(new Achievement(
                "First Steps", "Complete 5 tasks", AchievementType.MILESTONE, 5));
        achievementRepository.save(new Achievement(
                "Getting Started", "Complete 10 tasks", AchievementType.MILESTONE, 10));
        achievementRepository.save(new Achievement(
                "Halfway Hero", "Complete 50 tasks", AchievementType.MILESTONE, 50));
        achievementRepository.save(new Achievement(
                "Century Club", "Complete 100 tasks", AchievementType.MILESTONE, 100));
        achievementRepository.save(new Achievement(
                "Double Century", "Complete 200 tasks", AchievementType.MILESTONE, 200));
        achievementRepository.save(new Achievement(
                "Legendary", "Complete 500 tasks", AchievementType.MILESTONE, 500));

        // Streaks
        achievementRepository.save(new Achievement(
                "3 Day Streak", "Complete all tasks 3 days in a row", AchievementType.STREAK, 3));
        achievementRepository.save(new Achievement(
                "Week Warrior", "Complete all tasks 7 days in a row", AchievementType.STREAK, 7));
        achievementRepository.save(new Achievement(
                "Consistency King", "Complete all tasks 30 days in a row", AchievementType.STREAK, 30));

        // Category-specific
        achievementRepository.save(new Achievement(
                "Bookworm", "Complete 20 Study tasks", AchievementType.CATEGORY_MILESTONE, 20,
                "STUDY", null));
        achievementRepository.save(new Achievement(
                "Assignment Ace", "Complete 20 Assignment tasks", AchievementType.CATEGORY_MILESTONE, 20,
                "ASSIGNMENT", null));
        achievementRepository.save(new Achievement(
                "Project Pro", "Complete 20 Project tasks", AchievementType.CATEGORY_MILESTONE, 20,
                "PROJECT", null));

        // Priority-specific
        achievementRepository.save(new Achievement(
                "High Achiever", "Complete 15 High priority tasks", AchievementType.PRIORITY_MILESTONE, 15,
                null, "HIGH"));
    }
}