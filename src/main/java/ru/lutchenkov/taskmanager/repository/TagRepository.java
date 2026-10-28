package ru.lutchenkov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lutchenkov.taskmanager.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}