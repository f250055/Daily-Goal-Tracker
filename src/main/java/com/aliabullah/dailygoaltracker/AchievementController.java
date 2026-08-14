package com.aliabullah.dailygoaltracker;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    private static final ZoneId PAKISTAN_ZONE = ZoneId.of("Asia/Karachi");
    private final AchievementRepository achievementRepository;
    private final TaskRepository taskRepository;

    public AchievementController(AchievementRepository achievementRepository,TaskRepository taskRepository) {
        this.achievementRepository = achievementRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public Map<String, Object> getAchievements() {
        List<Achievement> newlyUnlocked = checkAndUnlockAchievements();
        List<Achievement> all = achievementRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("achievements", all);
        response.put("newlyUnlocked", newlyUnlocked);
        return response;
    }

    private List<Achievement> checkAndUnlockAchievements() {

        List<Task> allTasks = taskRepository.findAll();
        int completedCount = 0;
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                completedCount++;
            }
        }

        int currentStreak = calculateCurrentStreak();

        List<Achievement> achievements = achievementRepository.findAll();
        List<Achievement> newlyUnlocked = new ArrayList<>();

        for (Achievement achievement : achievements) {

            if (achievement.isUnlocked()) {
                continue;
            }

            boolean shouldUnlock = false;

            if (achievement.getType() == AchievementType.MILESTONE
                    && completedCount >= achievement.getThreshold()) {
                shouldUnlock = true;
            }

            if (achievement.getType() == AchievementType.STREAK
                    && currentStreak >= achievement.getThreshold()) {
                shouldUnlock = true;
            }

            if (achievement.getType() == AchievementType.CATEGORY_MILESTONE) {
                int categoryCompleted = 0;
                for (Task task : allTasks) {
                    if (task.isCompleted()
                            && task.getCategory() != null
                            && task.getCategory().toString().equals(achievement.getTargetCategory())) {
                        categoryCompleted++;
                    }
                }
                if (categoryCompleted >= achievement.getThreshold()) {
                    shouldUnlock = true;
                }
            }

            if (achievement.getType() == AchievementType.PRIORITY_MILESTONE) {
                int priorityCompleted = 0;
                for (Task task : allTasks) {
                    if (task.isCompleted()
                            && task.getPriority() != null
                            && task.getPriority().toString().equals(achievement.getTargetPriority())) {
                        priorityCompleted++;
                    }
                }
                if (priorityCompleted >= achievement.getThreshold()) {
                    shouldUnlock = true;
                }
            }

            if (shouldUnlock) {
                achievement.setUnlocked(true);
                achievement.setUnlockedDate(LocalDate.now(PAKISTAN_ZONE).toString());
                achievementRepository.save(achievement);
                newlyUnlocked.add(achievement);
            }
        }

        return newlyUnlocked;
    }

    private int calculateCurrentStreak() {

        LocalDate today = LocalDate.now(PAKISTAN_ZONE);
        int streak = 0;

        for (int i = 0; i < 365; i++) {
            LocalDate date = today.minusDays(i);
            List<Task> dayTasks = taskRepository.findByTaskDate(date.toString());

            if (dayTasks.isEmpty()) {
                break;
            }

            boolean allCompleted = true;
            for (Task task : dayTasks) {
                if (!task.isCompleted()) {
                    allCompleted = false;
                    break;
                }
            }

            if (!allCompleted) {
                break;
            }

            streak++;
        }

        return streak;
    }
}