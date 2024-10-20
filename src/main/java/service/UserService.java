package service;

import exception.UserException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import domain.User;
import repository.TagRepositoryInterface;
import repository.UserRepository;
import repository.UserRepositoryInterface;

import java.util.List;
import java.util.Optional;

public class UserService {


    private final UserRepositoryInterface userRepository;

    public UserService(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    public User insertUser(User user) {
        if (user == null) {
            throw new UserException("User cannot be null.");
        }
        return userRepository.insertUser(user);
    }

    public User updateUser(User user) {
        if (user == null) {
            throw new UserException("User cannot be null.");
        }
        return userRepository.updateUser(user);
    }

    public boolean deleteUser(Long userId) {
        if (userId == null) {
            throw new UserException("User ID cannot be null.");
        }
        return userRepository.deleteUser(userId); // Ensure this method returns boolean.
    }

    public Optional<User> findUserById(Long userId) {
        if (userId == null) {
            throw new UserException("User ID cannot be null.");
        }
        return userRepository.findUserById(userId); // Directly return Optional from repository.
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Optional<User> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new UserException("Email cannot be null or empty.");
        }
        return userRepository.findByEmail(email); // Directly return Optional from repository.
    }

    public void updateUserTokens(Long userId, Integer dailyTokens, Integer monthlyTokens) {
        if (userId == null) {
            throw new UserException("User ID cannot be null.");
        }

        Optional<User> optionalUser = userRepository.findUserById(userId);
        User user = optionalUser.orElseThrow(() -> new UserException("User not found with ID: " + userId));

        if (dailyTokens != null) {
            user.setDailyTokens(dailyTokens);
        }
        if (monthlyTokens != null) {
            user.setMonthlyTokens(monthlyTokens);
        }
        userRepository.updateUser(user); // Ensure this updates the user properly.
    }
}
