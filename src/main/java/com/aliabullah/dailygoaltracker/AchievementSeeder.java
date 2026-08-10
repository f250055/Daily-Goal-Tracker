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

        achievementRepository.save(new Achievement(
                "Getting Started", "Complete 10 tasks", AchievementType.MILESTONE, 10));
        achievementRepository.save(new Achievement(
                "Halfway Hero", "Complete 50 tasks", AchievementType.MILESTONE, 50));
        achievementRepository.save(new Achievement(
                "Century Club", "Complete 100 tasks", AchievementType.MILESTONE, 100));

        achievementRepository.save(new Achievement(
                "3 Day Streak", "Complete all tasks 3 days in a row", AchievementType.STREAK, 3));
        achievementRepository.save(new Achievement(
                "Week Warrior", "Complete all tasks 7 days in a row", AchievementType.STREAK, 7));
        achievementRepository.save(new Achievement(
                "Consistency King", "Complete all tasks 30 days in a row", AchievementType.STREAK, 30));
    }
}