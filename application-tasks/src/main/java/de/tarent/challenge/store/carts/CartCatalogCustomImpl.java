package de.tarent.challenge.store.carts;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import de.tarent.challenge.store.users.User;

@Repository
public class CartCatalogCustomImpl implements CartCatalogCustom{
	
	@PersistenceContext
    EntityManager entityManager;

	@Override
	public Cart findByUserAndStatus(User user, CartStatus status) {
		Cart cart = null;
	    Query query = entityManager.createQuery("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems ci WHERE c.user=:user and c.status=:status");
	    query.setParameter("user", user);
	    query.setParameter("status", status.toString());
	   
	    cart = (Cart) query.getSingleResult();
	    
	    return cart;
	}
	


	
}
