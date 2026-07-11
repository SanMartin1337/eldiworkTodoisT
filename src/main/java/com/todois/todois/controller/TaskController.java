package com.todois.todois.controller;

import com.todois.todois.entity.Task;
import com.todois.todois.service.ProjectService;
import com.todois.todois.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;

    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) String filter,
                       @RequestParam(required = false) Long projectId,
                       Model model) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate afterTomorrow = today.plusDays(2);

        List<Task> tasks = taskService.getAllTasks();

        // Фильтр по проекту
        if (projectId != null) {
            tasks = tasks.stream().filter(t -> projectId.equals(t.getProjectId())).collect(java.util.stream.Collectors.toList());
        }

        // Фильтр по статусу
        if ("active".equals(filter)) {
            tasks = tasks.stream().filter(t -> !"DONE".equals(t.getStatus())).collect(java.util.stream.Collectors.toList());
        } else if ("done".equals(filter)) {
            tasks = tasks.stream().filter(t -> "DONE".equals(t.getStatus())).collect(java.util.stream.Collectors.toList());
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("tasksToday", taskService.getTasksByDate(today));
        model.addAttribute("tasksTomorrow", taskService.getTasksByDate(tomorrow));
        model.addAttribute("tasksAfterTomorrow", taskService.getTasksByDate(afterTomorrow));
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("projectMap", taskService.getProjectMap());
        model.addAttribute("currentFilter", filter != null ? filter : "all");
        model.addAttribute("selectedProjectId", projectId);

        return "home";
    }

    @PostMapping("/tasks")
    public String createTask(@RequestParam String description,
                             @RequestParam(defaultValue = "MEDIUM") String priority,
                             @RequestParam(required = false) LocalDateTime dueDate,
                             @RequestParam(defaultValue = "Общие") String projectName) {
        taskService.createTask(description, priority, dueDate, projectName);
        return "redirect:/";
    }

    @GetMapping("/tasks/{id}/complete")
    public String completeTask(@PathVariable Long id) {
        taskService.completeTask(id);
        return "redirect:/";
    }

    @GetMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }
}