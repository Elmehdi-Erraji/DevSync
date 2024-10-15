package repository;

import domain.TokenLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class TokenLogRepository {

    private final EntityManager entityManager;

    public TokenLogRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<TokenLog> findAll() {
        TypedQuery<TokenLog> query = entityManager.createQuery("SELECT t FROM TokenLog t", TokenLog.class);
        return query.getResultList();
    }

    public TokenLog findById(Long id) {
        return entityManager.find(TokenLog.class, id);
    }

    public void save(TokenLog tokenLog) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (tokenLog.getId() == null) {
                entityManager.persist(tokenLog);
            } else {
                entityManager.merge(tokenLog);
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
            TokenLog tokenLog = entityManager.find(TokenLog.class, id);
            if (tokenLog != null) {
                entityManager.remove(tokenLog);
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
