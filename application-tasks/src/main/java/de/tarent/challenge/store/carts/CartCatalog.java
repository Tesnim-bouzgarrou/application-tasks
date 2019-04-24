package de.tarent.challenge.store.carts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartCatalog extends JpaRepository<Cart, Long>, CartCatalogCustom {

    Cart findByUser(Long idUser);

}
