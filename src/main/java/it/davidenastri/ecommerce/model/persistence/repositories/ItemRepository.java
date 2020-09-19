package it.davidenastri.ecommerce.model.persistence.repositories;

import it.davidenastri.ecommerce.model.persistence.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
	List<Item> findByName(String name);

}
