package it.davidenastri.ecommerce.controllers;

import it.davidenastri.ecommerce.TestUtils;
import it.davidenastri.ecommerce.model.persistence.Cart;
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

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("anotherUser")).thenReturn(null);

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

    @Test
    public void create_user_error_short_password() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("testUser");
        r.setPassword("test");
        r.setConfirmPassword("test");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void create_user_password_confirm_not_equal() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("testUser");
        r.setPassword("testPassword");
        r.setConfirmPassword("testWrongConfirmPassword");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_name_happy_path() {
        final ResponseEntity<User> response = userController.findByUserName("testUser");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals("test", u.getUsername());
    }

    @Test
    public void find_user_by_name_not_found() {
        final ResponseEntity<User> response = userController.findByUserName("anotherUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_id_happy_path() {
        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        ;
    }

    @Test
    public void find_user_by_id_not_found() {
        final ResponseEntity<User> response = userController.findById(33L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
