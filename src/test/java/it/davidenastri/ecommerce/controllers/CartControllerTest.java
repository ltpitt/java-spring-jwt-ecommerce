package it.davidenastri.ecommerce.controllers;

import it.davidenastri.ecommerce.TestUtils;
import it.davidenastri.ecommerce.model.persistence.Cart;
import it.davidenastri.ecommerce.model.persistence.Item;
import it.davidenastri.ecommerce.model.persistence.User;
import it.davidenastri.ecommerce.model.persistence.repositories.CartRepository;
import it.davidenastri.ecommerce.model.persistence.repositories.ItemRepository;
import it.davidenastri.ecommerce.model.persistence.repositories.UserRepository;
import it.davidenastri.ecommerce.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {

        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        Cart cart = new Cart();
        User user = new User();
        user.setId(0);
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("A Widget");
        BigDecimal price = BigDecimal.valueOf(9.99);
        item.setPrice(price);
        item.setDescription("A widget description");
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void add_to_cart_invalid_user() {

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("boo");
        ResponseEntity<Cart> response = cartController.addToCart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void add_to_cart_invalid_item() {

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(2L);
        r.setQuantity(1);
        r.setUsername("testUser");
        ResponseEntity<Cart> response = cartController.addToCart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void add_to_cart_happy_path() {

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("testUser");
        ResponseEntity<Cart> response = cartController.addToCart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(BigDecimal.valueOf(9.99), c.getTotal());

    }

    @Test
    public void remove_from_cart_invalid_user() {

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("boo");
        ResponseEntity<Cart> response = cartController.removeFromCart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void remove_from_cart_invalid_item() {

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(2L);
        r.setQuantity(1);
        r.setUsername("testUser");
        ResponseEntity<Cart> response = cartController.removeFromCart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void remove_from_cart_happy_path() {

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(2);
        r.setUsername("testUser");
        ResponseEntity<Cart> response = cartController.addToCart(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("testUser");
        response = cartController.removeFromCart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(BigDecimal.valueOf(9.99), c.getTotal());

    }

}