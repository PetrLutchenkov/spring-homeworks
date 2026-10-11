package ru.lutchenkov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lutchenkov.taskmanager.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}