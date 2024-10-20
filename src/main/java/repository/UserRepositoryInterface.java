package repository;

import domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryInterface {
    User insertUser(User user);

    User updateUser(User user);

    boolean deleteUser(Long userId);

    Optional<User> findUserById(Long userId);

    List<User> findAllUsers();

    Optional<User> findByEmail(String email);
}
