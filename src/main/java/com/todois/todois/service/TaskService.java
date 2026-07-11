package com.todois.todois.service;

import com.todois.todois.entity.Project;
import com.todois.todois.entity.Task;
import com.todois.todois.repository.ProjectRepository;
import com.todois.todois.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectService projectService, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.projectRepository = projectRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByCreatedAtDesc();
    }

    // Задачи на конкретный день (сравниваем только дату)
    public List<Task> getTasksByDate(LocalDate date) {
        return getAllTasks().stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    public Map<Long, Project> getProjectMap() {
        return StreamSupport.stream(projectRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(Project::getId, p -> p));
    }

    public Task createTask(String description, String priority, LocalDateTime dueDateTime, String projectName) {
        Long projectId = null;
        if (projectName != null && !projectName.isBlank()) {
            projectId = projectService.getOrCreateProject(projectName).getId();
        }
        Task task = new Task(description, priority, "ACTIVE", dueDateTime, projectId);
        return taskRepository.save(task);
    }

    public void completeTask(Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setStatus("DONE");
            taskRepository.save(task);
        });
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}