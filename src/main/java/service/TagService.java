package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import domain.Tag;
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
        tagRepository.save(tag); // Save method handles both insert and update
    }

    public void updateTag(Tag tag) {
        tagRepository.save(tag); // Use the same save method for updating
    }

    public void deleteTag(Long tagId) {
        tagRepository.delete(tagId);
    }

    public Tag findTagById(Long tagId) {
        return tagRepository.findById(tagId);
    }

    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }
}
