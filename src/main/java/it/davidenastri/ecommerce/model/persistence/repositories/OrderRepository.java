package it.davidenastri.ecommerce.model.persistence.repositories;

import it.davidenastri.ecommerce.model.persistence.User;
import it.davidenastri.ecommerce.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
