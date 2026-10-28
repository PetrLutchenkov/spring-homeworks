package ru.lutchenkov.taskmanager.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lutchenkov.taskmanager.exception.TagNotFoundException;
import ru.lutchenkov.taskmanager.model.Tag;
import ru.lutchenkov.taskmanager.repository.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id));
    }

    @Transactional
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag updateTag(Long id, Tag updatedTag) {
        Tag existingTag = getTagById(id);
        existingTag.setName(updatedTag.getName());
        return existingTag;
    }

    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException(id);
        }
        tagRepository.deleteById(id);
    }
}