package ru.lutchenkov.taskmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lutchenkov.taskmanager.dto.CommentDto;
import ru.lutchenkov.taskmanager.dto.CreateCommentRequest;
import ru.lutchenkov.taskmanager.dto.UpdateCommentRequest;
import ru.lutchenkov.taskmanager.model.Comment;
import ru.lutchenkov.taskmanager.model.Task;
import ru.lutchenkov.taskmanager.model.User;
import ru.lutchenkov.taskmanager.service.CommentService;
import ru.lutchenkov.taskmanager.service.TaskService;
import ru.lutchenkov.taskmanager.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> comments = commentService.getAllComments().stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        return ResponseEntity.ok(mapToDto(comment));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentRequest request) {
        User user = userService.getUserById(request.getAuthorId());
        Task task = taskService.getTaskById(request.getTaskId());

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setTask(task);
        comment.setUser(user);

        Comment createdComment = commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(createdComment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @Valid @RequestBody UpdateCommentRequest request) {
        Comment commentData = new Comment();
        commentData.setText(request.getText());

        Comment updatedComment = commentService.updateComment(id, commentData);
        return ResponseEntity.ok(mapToDto(updatedComment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAt());
        if (comment.getTask() != null) {
            dto.setTaskId(comment.getTask().getId());
        }
        if (comment.getUser() != null) {
            dto.setAuthorId(comment.getUser().getId());
            dto.setAuthorName(comment.getUser().getUsername());
        }

        return dto;
    }
}
