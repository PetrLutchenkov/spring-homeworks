package ru.lutchenkov.taskmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lutchenkov.taskmanager.dto.CreateTagRequest;
import ru.lutchenkov.taskmanager.dto.TagDto;
import ru.lutchenkov.taskmanager.dto.UpdateTagRequest;
import ru.lutchenkov.taskmanager.model.Tag;
import ru.lutchenkov.taskmanager.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tags = tagService.getAllTags().stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        Tag tag = tagService.getTagById(id);
        return ResponseEntity.ok(mapToDto(tag));
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody CreateTagRequest request) {
        Tag tag = new Tag();
        tag.setName(request.getName());

        Tag createdTag = tagService.createTag(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(createdTag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateTag(@PathVariable Long id, @Valid @RequestBody UpdateTagRequest request) {
        Tag tagData = new Tag();
        tagData.setName(request.getName());

        Tag updatedTag = tagService.updateTag(id, tagData);
        return ResponseEntity.ok(mapToDto(updatedTag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    private TagDto mapToDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}