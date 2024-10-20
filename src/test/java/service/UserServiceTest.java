package service;

import domain.User;
import exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test cases for inserting users

    @Test
    public void testInsertUser_ValidUser() {
        User user = new User();
        user.setEmail("user@example.com");

        when(userRepository.insertUser(user)).thenReturn(user);

        User result = userService.insertUser(user);
        assertEquals(user, result);
        verify(userRepository, times(1)).insertUser(user);
    }

    @Test
    public void testInsertUser_NullUser() {
        UserException exception = assertThrows(
                UserException.class,
                () -> userService.insertUser(null)
        );

        assertEquals("User cannot be null.", exception.getMessage());
        verify(userRepository, never()).insertUser(any(User.class));
    }



    @Test
    public void testUpdateUser_ValidUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        when(userRepository.updateUser(user)).thenReturn(user);

        User result = userService.updateUser(user);
        assertEquals(user, result);
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    public void testUpdateUser_NullUser() {
        UserException exception = assertThrows(
                UserException.class,
                () -> userService.updateUser(null)
        );

        assertEquals("User cannot be null.", exception.getMessage());
        verify(userRepository, never()).updateUser(any(User.class));
    }


    @Test
    public void testFindUserById_ValidId() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1L);
        assertEquals(Optional.of(user), result);
        verify(userRepository, times(1)).findUserById(1L);
    }

    @Test
    public void testFindUserById_NullId() {
        UserException exception = assertThrows(
                UserException.class,
                () -> userService.findUserById(null)
        );

        assertEquals("User ID cannot be null.", exception.getMessage());
        verify(userRepository, never()).findUserById(any(Long.class));
    }

    // Test case for deleting users

    @Test
    public void testDeleteUser_ValidId() {
        when(userRepository.deleteUser(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);
        assertEquals(true, result);
        verify(userRepository, times(1)).deleteUser(1L);
    }

    @Test
    public void testDeleteUser_NullId() {
        UserException exception = assertThrows(
                UserException.class,
                () -> userService.deleteUser(null)
        );

        assertEquals("User ID cannot be null.", exception.getMessage());
        verify(userRepository, never()).deleteUser(any(Long.class));
    }
}
