package service;


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
        userRepository.insertUser(user);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

    public User findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserTokens(Long userId, Integer dailyTokens, Integer monthlyTokens) {
        User user = userRepository.findUserById(userId);
        if (dailyTokens != null) {
            user.setDailyTokens(dailyTokens);
        }
        if (monthlyTokens != null) {
            user.setMonthlyTokens(monthlyTokens);
        }
        userRepository.insertUser(user);
    }

    public void updateDailyTokens(Long userId, Integer dailyTokens) {
        User user = userRepository.findUserById(userId);
        if (dailyTokens != null) {
            user.setDailyTokens(dailyTokens);
            userRepository.insertUser(user); // Save the updated user
        }
    }

    // New method to update only monthly tokens
    public void updateMonthlyTokens(Long userId, Integer monthlyTokens) {
        User user = userRepository.findUserById(userId);
        if (monthlyTokens != null) {
            user.setMonthlyTokens(monthlyTokens);
            userRepository.insertUser(user); // Save the updated user
        }
    }

}
