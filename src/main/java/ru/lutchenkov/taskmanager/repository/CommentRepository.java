package ru.lutchenkov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lutchenkov.taskmanager.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}