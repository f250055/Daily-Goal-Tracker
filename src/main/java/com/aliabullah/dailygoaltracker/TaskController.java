package com.aliabullah.dailygoaltracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final ZoneId PAKISTAN_ZONE = ZoneId.of("Asia/Karachi");

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task) {

        LocalTime now = LocalTime.now(PAKISTAN_ZONE);
        LocalTime cutoff = LocalTime.of(23, 0);

        if (now.isAfter(cutoff)) {
            return ResponseEntity.status(403).build();
        }

        if (task.getDescription() == null || task.getDescription().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (task.getPriority() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (task.getCategory() == null) {
            return ResponseEntity.badRequest().build();
        }

        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Task> updateTask(@PathVariable int id,
                                           @RequestBody Task updatedTask) {

        Task task = taskRepository.findById(id).orElseThrow();

        if (!task.getTaskDate().equals(LocalDate.now(PAKISTAN_ZONE).toString())) {
            return ResponseEntity.status(403).build();
        }

        task.setCompleted(updatedTask.isCompleted());

        if (updatedTask.getDescription() != null && !updatedTask.getDescription().isBlank()) {
            task.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getPriority() != null) {
            task.setPriority(updatedTask.getPriority());
        }
        if (updatedTask.getCategory() != null) {
            task.setCategory(updatedTask.getCategory());
        }
        task.setDueDate(updatedTask.getDueDate());

        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @PatchMapping("/{id}")
    public Task completeTask(@PathVariable int id) {

        Task task = taskRepository.findById(id).orElseThrow();

        task.setCompleted(!task.isCompleted());

        return taskRepository.save(task);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable int id) {

        return taskRepository.findById(id).orElseThrow();
    }

    @GetMapping(params = "date")
    public List<Task> getTasksByDate(@RequestParam String date) {

        return taskRepository.findByTaskDate(date);
    }
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        List<Task> allTasks = taskRepository.findAll();

        int total = allTasks.size();
        int completed = 0;
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                completed++;
            }
        }
        int pending = total - completed;
        double completionRate = total == 0 ? 0 : Math.round((completed * 10000.0) / total) / 100.0;

        int currentStreak = calculateCurrentStreak();

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("completed", completed);
        stats.put("pending", pending);
        stats.put("completionRate", completionRate);
        stats.put("currentStreak", currentStreak);
        return stats;
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
    @GetMapping("/wall")
    public List<Map<String, Object>> getWall(@RequestParam(defaultValue = "30") int days) {
        List<Map<String, Object>> wall = new ArrayList<>();
        LocalDate today = LocalDate.now(PAKISTAN_ZONE);

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            List<Task> dayTasks = taskRepository.findByTaskDate(date.toString());

            int total = dayTasks.size();
            int completed = 0;
            for (Task task : dayTasks) {
                if (task.isCompleted()) {
                    completed++;
                }
            }

            String status;
            if (total == 0) {
                status = "NONE";
            } else if (completed == total) {
                status = "PERFECT";
            } else if (completed > 0) {
                status = "PARTIAL";
            } else {
                status = "MISSED";
            }

            Map<String, Object> dayEntry = new HashMap<>();
            dayEntry.put("date", date.toString());
            dayEntry.put("total", total);
            dayEntry.put("completed", completed);
            dayEntry.put("status", status);
            wall.add(dayEntry);
        }

        return wall;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {

        taskRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}