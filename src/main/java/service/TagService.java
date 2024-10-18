// TagService.java
package service;

import domain.Tag;
import exception.TagException;
import jakarta.validation.Valid;
import repository.TagRepositoryInterface; // Use the interface

import java.util.List;
import java.util.Optional;

public class TagService {

    private final TagRepositoryInterface tagRepository; // Change to the interface type

    // Constructor injection
    public TagService(TagRepositoryInterface tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag insertTag(Tag tag) {
        validateTag(tag);
        return tagRepository.save(tag);
    }

    public Tag updateTag(Tag tag) {
        if (tag.getId() == null) {
            throw new TagException("Tag ID cannot be null when updating a tag.");
        }
        validateTag(tag);
        return tagRepository.save(tag);
    }

    public boolean deleteTag(Long tagId) {
        if (tagId == null) {
            throw new TagException("Tag ID cannot be null.");
        }
        Optional<Tag> existingTag = tagRepository.findById(tagId);
        if (existingTag.isEmpty()) {
            throw new TagException("Tag not found with ID: " + tagId);
        }
        return tagRepository.delete(tagId);
    }

    public Optional<Tag> findTagById(Long tagId) {
        if (tagId == null) {
            throw new TagException("Tag ID cannot be null.");
        }
        return tagRepository.findById(tagId);
    }

    public Optional<List<Tag>> findAllTags() {
        return tagRepository.findAll();
    }

    private void validateTag(Tag tag) {
        if (tag == null) {
            throw new TagException("Tag cannot be null.");
        }
        if (tag.getName() == null || tag.getName().trim().isEmpty()) {
            throw new TagException("Tag name cannot be null or empty.");
        }
        if (tag.getName().length() > 50) {
            throw new TagException("Tag name cannot exceed 50 characters.");
        }
    }
}
