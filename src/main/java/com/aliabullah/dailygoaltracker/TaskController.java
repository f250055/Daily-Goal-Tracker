package com.aliabullah.dailygoaltracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        LocalTime now = LocalTime.now();
        LocalTime cutoff = LocalTime.of(9, 0);

        if (now.isAfter(cutoff)) {
            return ResponseEntity.status(403).build();
        }
        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Task task = taskRepository.findById(id).orElseThrow();

        if (!task.getTaskDate().equals(LocalDate.now())) {
            return ResponseEntity.status(403).build();
        }

        task.setCompleted(updatedTask.isCompleted());
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
        LocalDate parsedDate = LocalDate.parse(date);
        return taskRepository.findByTaskDate(parsedDate);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
