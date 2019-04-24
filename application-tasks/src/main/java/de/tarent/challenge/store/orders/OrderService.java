package de.tarent.challenge.store.orders;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tarent.challenge.store.carts.Cart;
import de.tarent.challenge.store.carts.CartItem;
import de.tarent.challenge.store.carts.dto.CartItemDTO;
import de.tarent.challenge.store.exceptions.CartAlreadyExistForUser;
import de.tarent.challenge.store.exceptions.CartWithoutItemsException;
import de.tarent.challenge.store.exceptions.NoCurrentCartException;
import de.tarent.challenge.store.exceptions.StoreException;
import de.tarent.challenge.store.exceptions.UserNotFoundException;
import de.tarent.challenge.store.products.Product;
import de.tarent.challenge.store.products.ProductCatalog;
import de.tarent.challenge.store.users.User;
import de.tarent.challenge.store.users.UserCatalog;

@Service
public class OrderService {

	private final OrderCatalog orderCatalog;

//	@Autowired
//	private ProductCatalog productCatalog;
//
//	@Autowired
//	private UserCatalog userCatalog;

	public OrderCatalog getOrderCatalog() {
		return orderCatalog;
	}

	@PersistenceContext
	EntityManager entityManager;

	public OrderService(OrderCatalog orderCatalog) {
		this.orderCatalog = orderCatalog;
	}

	public Order createOrderFromCart(Cart cart) {
		Set<OrderItem> items = new HashSet<OrderItem>();
		
		for (CartItem cartItem : cart.getCartItems()) {
			OrderItem item = new OrderItem(cartItem.getProduct(), cartItem.getQuantity(), cartItem.getPrice());
			items.add(item);
		}
		
		Order order = new Order(cart.getUser(), items);
		order.setCreatedAt(new Date());
		order.setStatus(OrderStatus.CREATED);
		
		return order;
	}

	
}
