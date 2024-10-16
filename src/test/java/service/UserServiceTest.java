package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceTest {

    @Test
    public void testUserServiceCreation() {
        UserService userService = new UserService();
        assertNotNull(userService, "UserService should be created successfully.");
    }
}
