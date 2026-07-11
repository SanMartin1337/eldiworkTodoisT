package com.todois.todois.service;

import com.todois.todois.entity.Project;
import com.todois.todois.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Все проекты (по алфавиту)
    public List<Project> getAllProjects() {
        return projectRepository.findAllByOrderByNameAsc();
    }

    // Получить проект по имени или создать новый
    public Project getOrCreateProject(String name) {
        return projectRepository.findByName(name)
                .orElseGet(() -> projectRepository.save(new Project(name)));
    }
}