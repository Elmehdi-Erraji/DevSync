package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import domain.User;

import java.util.List;
import java.util.Optional;

public class UserRepository {

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User insertUser(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to insert user: " + e.getMessage(), e);
        }
    }

    public User updateUser(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            System.out.println("Updating user: " + user);

            // Ensure the user exists and is attached to the EntityManager
            User existingUser = entityManager.find(User.class, user.getId());
            if (existingUser == null) {
                throw new RuntimeException("User not found for ID: " + user.getId());
            }

            // Update only the fields that changed
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());

            User updatedUser = entityManager.merge(existingUser);
            transaction.commit();

            System.out.println("User updated successfully: " + updatedUser);
            return updatedUser;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                System.out.println("Transaction rolled back due to error.");
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }


    public boolean deleteUser(Long userId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User user = entityManager.find(User.class, userId);
            if (user != null) {
                entityManager.remove(user);
                transaction.commit();
                return true; // Indicate successful deletion.
            }
            transaction.commit();
            return false; // Indicate user was not found.
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false; // Indicate failure.
        }
    }

    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(entityManager.find(User.class, userId)); // Return Optional<User>
    }

    public List<User> findAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public Optional<User> findByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return Optional.of(query.getSingleResult()); // Return the user wrapped in Optional
        } catch (Exception e) {
            return Optional.empty(); // Return an empty Optional if no user is found
        }
    }
}
