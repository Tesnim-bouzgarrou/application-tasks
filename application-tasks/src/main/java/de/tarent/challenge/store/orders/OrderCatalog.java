package de.tarent.challenge.store.orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCatalog extends JpaRepository<Order, Long> {


}
