package com.todois.todois.repository;

import com.todois.todois.entity.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Long> {

    List<Project> findAllByOrderByNameAsc();

    Optional<Project> findByName(String name);
}