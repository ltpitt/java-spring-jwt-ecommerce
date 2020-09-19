package it.davidenastri.ecommerce.model.persistence.repositories;

import it.davidenastri.ecommerce.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
