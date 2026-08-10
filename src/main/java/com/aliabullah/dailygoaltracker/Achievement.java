package com.aliabullah.dailygoaltracker;

import jakarta.persistence.*;

@Entity
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private AchievementType type;

    private int threshold;
    private boolean unlocked;
    private String unlockedDate;

    public Achievement() {
    }

    public Achievement(String name, String description, AchievementType type, int threshold) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.threshold = threshold;
        this.unlocked = false;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public AchievementType getType() {
        return type;
    }
    public int getThreshold() {
        return threshold;
    }
    public boolean isUnlocked() {
        return unlocked;
    }
    public String getUnlockedDate() {
        return unlockedDate;
    }
    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
    public void setUnlockedDate(String unlockedDate) {
        this.unlockedDate = unlockedDate;
    }
}