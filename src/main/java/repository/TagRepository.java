package repository;

import domain.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TagRepository {

    private EntityManager entityManager;

    public TagRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Tag> findAll() {
        TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag t", Tag.class);
        return query.getResultList();
    }

    public Tag findById(Long id) {
        return entityManager.find(Tag.class, id);
    }

    public void save(Tag tag) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (tag.getId() == null) {
                entityManager.persist(tag);
            } else {
                entityManager.merge(tag);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Tag tag = entityManager.find(Tag.class, id);
            if (tag != null) {
                entityManager.remove(tag);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
