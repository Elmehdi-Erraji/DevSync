package service;

import domain.TokenLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import repository.TokenLogRepository;

import java.util.List;

public class TokenLogService {

    private final TokenLogRepository tokenLogRepository;

    public TokenLogService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("your-persistence-unit-name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.tokenLogRepository = new TokenLogRepository(entityManager);
    }

    public void saveTokenLog(TokenLog tokenLog) {
        validateTokenLog(tokenLog);
        tokenLogRepository.save(tokenLog);
    }

    public void updateTokenLog(TokenLog tokenLog) {
        if (tokenLog.getId() == null) {
            throw new RuntimeException("TokenLog ID cannot be null when updating.");
        }
        validateTokenLog(tokenLog);
        tokenLogRepository.save(tokenLog);
    }

    public void deleteTokenLog(Long id) {
        if (id == null) {
            throw new RuntimeException("TokenLog ID cannot be null.");
        }
        TokenLog existingLog = tokenLogRepository.findById(id);
        if (existingLog == null) {
            throw new RuntimeException("TokenLog not found with ID: " + id);
        }
        tokenLogRepository.delete(id);
    }

    public TokenLog findTokenLogById(Long id) {
        if (id == null) {
            throw new RuntimeException("TokenLog ID cannot be null.");
        }
        TokenLog tokenLog = tokenLogRepository.findById(id);
        if (tokenLog == null) {
            throw new RuntimeException("TokenLog not found with ID: " + id);
        }
        return tokenLog;
    }

    public List<TokenLog> findAllTokenLogs() {
        return tokenLogRepository.findAll();
    }

    private void validateTokenLog(TokenLog tokenLog) {
        if (tokenLog == null) {
            throw new RuntimeException("TokenLog cannot be null.");
        }
        if (tokenLog.getTokensUsed() < 0) {
            throw new RuntimeException("Tokens used cannot be negative.");
        }
        if (tokenLog.getUsername() == null || tokenLog.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username cannot be null or empty.");
        }
    }
}
