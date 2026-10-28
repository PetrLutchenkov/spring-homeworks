package ru.lutchenkov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lutchenkov.taskmanager.exception.CommentNotFoundException;
import ru.lutchenkov.taskmanager.model.Comment;
import ru.lutchenkov.taskmanager.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
    }

    @Transactional
    public Comment createComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long id, Comment updatedComment) {
        Comment existingComment = getCommentById(id);
        existingComment.setText(updatedComment.getText());
        return existingComment;
    }

    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException(id);
        }
        commentRepository.deleteById(id);
    }
}