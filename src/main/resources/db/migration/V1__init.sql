CREATE TABLE IF NOT EXISTS "tasks" (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       description VARCHAR(255) NOT NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    due_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    project_id BIGINT
    );

CREATE TABLE IF NOT EXISTS "projects" (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

ALTER TABLE "tasks" ADD FOREIGN KEY (project_id) REFERENCES "projects"(id);