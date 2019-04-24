package de.tarent.challenge.store.carts;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CartService {

	private final CartCatalog cartCatalog;
	

	@Autowired
	private ProductCatalog productCatalog;

	@Autowired
	private UserCatalog userCatalog;
	
	@PersistenceContext
    EntityManager entityManager;

	public CartService(CartCatalog productCatalog) {
		this.cartCatalog = productCatalog;
	}

	public Cart retrieveCurrentCartByUser(User user) throws StoreException {

		return cartCatalog.findByUserAndStatus(user, CartStatus.CREATED);
	}
	
	
	
	public Cart retrieveCurrentCartByUser(Long userId) throws StoreException {
		User user = userCatalog.findOne(userId);
		if (user == null) {
			throw new UserNotFoundException("User " + userId + " not found");
		}

		Cart cart =  cartCatalog.findByUserAndStatus(user, CartStatus.CREATED);
		if(cart == null) {
			throw new NoCurrentCartException("No cart for User "+user.getId()+" exists, please create one");
		}
		
		return cart;
	}

	public Cart createNewCartForUser(Long userId, Set<CartItemDTO> itemsDTO) throws StoreException {
		User user = userCatalog.findOne(userId);
		if (user == null) {
			throw new UserNotFoundException("User " + userId + " not found");
		}

		Cart currentCart = retrieveCurrentCartByUser(user);
		if (currentCart != null) {
			throw new CartAlreadyExistForUser("A new Cart cannot be created. The User has already one");
		}

		if (itemsDTO == null || itemsDTO.size() < 1) {
			throw new CartWithoutItemsException("Cart should contain at least one item");
		}

		Set<CartItem> items = createCartItemsFromDTO(itemsDTO);
		Cart cart = new Cart(user, items);
		
		cart = cartCatalog.save(cart);
	
		return cart;

	}

	private Set<CartItem> createCartItemsFromDTO(Set<CartItemDTO> itemsDTO) {
		Set<CartItem> items = new HashSet<CartItem>();
		for (CartItemDTO cartItemDTO : itemsDTO) {
			Product product = productCatalog.findBySku(cartItemDTO.getSku());

			if (product != null && cartItemDTO.getQuantity() > 0) {
				CartItem cartItem = new CartItem(product, cartItemDTO.getQuantity(), product.getPrice());
				items.add(cartItem);
			}
		}
		return items;
	}

	public Cart addItemToCart(Long idCart, String skuProduct, Long quantity) {

		Cart cart = cartCatalog.findOne(idCart);
		Product product = productCatalog.findBySku(skuProduct);
		cart.getCartItems().add(new CartItem(cart, product, quantity, product.getPrice()));
		cartCatalog.save(cart);
		return cart;
	}

	public Iterable<Cart> retrieveAllCarts() {
		return cartCatalog.findAll();
	}
	
	public Iterable<Cart> retrieveAllItems() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void deleteAllCarts() {
		cartCatalog.deleteAll();
	}

	public UserCatalog getUserCatalog() {
		return userCatalog;
	}

	public void setUserCatalog(UserCatalog userCatalog) {
		this.userCatalog = userCatalog;
	}

	public ProductCatalog getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(ProductCatalog productCatalog) {
		this.productCatalog = productCatalog;
	}

	

	

}
