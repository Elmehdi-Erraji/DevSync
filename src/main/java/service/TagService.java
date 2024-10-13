package service;

import domain.Tag;
import exception.TagException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import repository.TagRepository;

import java.util.List;

public class TagService {

    private final TagRepository tagRepository;

    public TagService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("your-persistence-unit-name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.tagRepository = new TagRepository(entityManager);
    }

    public void insertTag(Tag tag) {
        validateTag(tag);
        tagRepository.save(tag);
    }

    public void updateTag(Tag tag) {
        if (tag.getId() == null) {
            throw new TagException("Tag ID cannot be null when updating a tag.");
        }
        validateTag(tag);
        tagRepository.save(tag);
    }

    public void deleteTag(Long tagId) {
        if (tagId == null) {
            throw new TagException("Tag ID cannot be null.");
        }
        Tag existingTag = tagRepository.findById(tagId);
        if (existingTag == null) {
            throw new TagException("Tag not found with ID: " + tagId);
        }
        tagRepository.delete(tagId);
    }

    public Tag findTagById(Long tagId) {
        if (tagId == null) {
            throw new TagException("Tag ID cannot be null.");
        }
        Tag tag = tagRepository.findById(tagId);
        if (tag == null) {
            throw new TagException("Tag not found with ID: " + tagId);
        }
        return tag;
    }

    public List<Tag> findAllTags() {
        List<Tag> tags = tagRepository.findAll();
        if (tags.isEmpty()) {
            throw new TagException("No tags found.");
        }
        return tags;
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
