package it.davidenastri.ecommerce.controllers;

import it.davidenastri.ecommerce.TestUtils;
import it.davidenastri.ecommerce.model.persistence.User;
import it.davidenastri.ecommerce.model.persistence.repositories.CartRepository;
import it.davidenastri.ecommerce.model.persistence.repositories.UserRepository;
import it.davidenastri.ecommerce.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }


    @Test
    public void create_user_happy_path() throws Exception {

        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());

    }

}
