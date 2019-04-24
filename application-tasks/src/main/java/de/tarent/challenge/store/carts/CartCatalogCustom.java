package de.tarent.challenge.store.carts;

import de.tarent.challenge.store.users.User;

public interface CartCatalogCustom {

	 Cart findByUserAndStatus(User user, CartStatus status);
	 
	 
}
