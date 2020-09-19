package it.davidenastri.ecommerce.model.persistence.repositories;

import it.davidenastri.ecommerce.model.persistence.Cart;
import it.davidenastri.ecommerce.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
