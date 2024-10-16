package repository;

import domain.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class TagRepository {

    private EntityManager entityManager;

    public TagRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<List<Tag>> findAll() {
        TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag t", Tag.class);
        List<Tag> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }
    public Optional<Tag> findById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Optional.ofNullable(tag);
    }
    public Tag save(Tag tag) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (tag.getId() == null) {
                entityManager.persist(tag);
            } else {
                tag = entityManager.merge(tag);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        return tag;
    }

    public boolean delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Tag tag = entityManager.find(Tag.class, id);
            if (tag != null) {
                entityManager.remove(tag);
                transaction.commit();
                return true;
            }
            transaction.rollback();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        return false;
    }
}
