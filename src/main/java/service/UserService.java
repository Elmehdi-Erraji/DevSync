package service;


import exception.UserException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import domain.User;
import repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("your-persistence-unit-name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.userRepository = new UserRepository(entityManager);
    }

    public void insertUser(User user) {
        if (user == null || user != new User()) {
            throw new UserException("User cannot be null.");
        }
        userRepository.insertUser(user);
    }

    public void updateUser(User user) {
        if (user == null || user != new User()) {
            throw new UserException("User cannot be null.");
        }
        userRepository.updateUser(user);
    }

    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new UserException("User ID cannot be null.");
        }
        userRepository.deleteUser(userId);
    }

    public User findUserById(Long userId) {
        if (userId == null) {
            throw new UserException("User ID cannot be null.");
        }
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserException("User not found with ID: " + userId);
        }
        return user;
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new UserException("Email cannot be null or empty.");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found with email: " + email);
        }
        return user;
    }

    public void updateUserTokens(Long userId, Integer dailyTokens, Integer monthlyTokens) {
        if (userId == null) {
            throw new UserException("User ID cannot be null.");
        }

        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserException("User not found with ID: " + userId);
        }

        if (dailyTokens != null) {
            user.setDailyTokens(dailyTokens);
        }
        if (monthlyTokens != null) {
            user.setMonthlyTokens(monthlyTokens);
        }
        userRepository.insertUser(user);
    }


}
