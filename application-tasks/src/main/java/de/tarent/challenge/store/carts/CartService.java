package de.tarent.challenge.store.carts;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tarent.challenge.store.carts.dto.CartItemDTO;
import de.tarent.challenge.store.exceptions.CartAlreadyExistForUser;
import de.tarent.challenge.store.exceptions.CartNotForUserException;
import de.tarent.challenge.store.exceptions.CartWithoutItemsException;
import de.tarent.challenge.store.exceptions.NoCartFoundExcption;
import de.tarent.challenge.store.exceptions.NoCurrentCartException;
import de.tarent.challenge.store.exceptions.StoreException;
import de.tarent.challenge.store.exceptions.UserNotFoundException;
import de.tarent.challenge.store.orders.Order;
import de.tarent.challenge.store.orders.OrderCatalog;
import de.tarent.challenge.store.orders.OrderService;
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
	
	@Autowired
	private OrderService orderService;


	@PersistenceContext
	EntityManager entityManager;

	public CartService(CartCatalog productCatalog) {
		this.cartCatalog = productCatalog;
	}

	/**
	 * get current Cart given user
	 * 
	 * @param user
	 *            cart owner
	 * @return the current Car
	 */
	public Cart retrieveCurrentCartByUser(User user) {

		return cartCatalog.findByUserAndStatus(user, CartStatus.CREATED);
	}

	/**
	 * get current Cart given user Id
	 * 
	 * @param userId
	 *            Id of cart owner
	 * @return the current Cart
	 * @throws StoreException
	 *             thrown if user or cart not found
	 */
	public Cart retrieveCurrentCartByUser(Long userId) throws StoreException {
		User user = userCatalog.findOne(userId);
		if (user == null) {
			throw new UserNotFoundException("User " + userId + " not found");
		}

		Cart cart = cartCatalog.findByUserAndStatus(user, CartStatus.CREATED);
		if (cart == null) {
			throw new NoCurrentCartException("No open cart for User " + user.getId() + " exists, please create one");
		}

		return cart;
	}
	
	/**
	 * get cart by id and user
	 * @param userId
	 * @return
	 * @throws StoreException 
	 */
	public Cart retrieveCartByIdAndUser( Long cartId , Long userId) throws StoreException {
		Cart cart = cartCatalog.findOne(cartId);
		
		if(cart == null) {
			throw new NoCartFoundExcption("No cart found with ID "+cartId);
		}
		
		if(!cart.getUser().getId().equals(userId)) {
			throw new CartNotForUserException("the cart "+cartId+" doens't belong to the User "+userId);
		}
		
		return cart;
	}

	/**
	 * Create new Cart for User
	 * 
	 * @param userId
	 *            id user
	 * @param itemsDTO
	 *            Items of the new Cart
	 * @return new created Cart
	 * @throws StoreException
	 *             thrown if user not found or Cart empty
	 */
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

	/**
	 * Move Data from cartItemDTO to cartItem persisant entity
	 * 
	 * @param itemsDTO
	 *            List of cartItemDTO coming from the client
	 * @return list of cartItems
	 */
	private Set<CartItem> createCartItemsFromDTO(Set<CartItemDTO> itemsDTO) {
		Set<CartItem> items = new HashSet<CartItem>();
		for (CartItemDTO cartItemDTO : itemsDTO) {

			CartItem cartItem = createCartItemsFromDTO(cartItemDTO);
			if (cartItem != null) {
				items.add(cartItem);
			}
		}
		return items;
	}

	/**
	 * Move Data from Dto to peristant CartItem entity
	 * 
	 * @param cartItemDTO
	 * @return
	 */
	private CartItem createCartItemsFromDTO(CartItemDTO cartItemDTO) {
		CartItem cartItem = null;
		Product product = productCatalog.findBySku(cartItemDTO.getSku());
		if (product != null) {
			cartItem = new CartItem(product, cartItemDTO.getQuantity(), product.getPrice());
		}
		return cartItem;
	}

	/**
	 * Add item to cart
	 * 
	 * @param idCart
	 *            cart id
	 * @param skuProduct
	 *            the product to be added
	 * @param quantity
	 *            the quantity of product
	 * @return the updated Cart
	 */
	public Cart addItemToCart(Long idCart, String skuProduct, Long quantity) {

		Cart cart = cartCatalog.findOne(idCart);
		Product product = productCatalog.findBySku(skuProduct);
		cart.getCartItems().add(new CartItem(cart, product, quantity, product.getPrice()));
		cartCatalog.save(cart);
		return cart;
	}

	/**
	 * addItemToCurrentCartOfUser
	 * 
	 * @param cartItemDto
	 * @param userId
	 * @return
	 * @throws StoreException
	 */
	public Cart addItemToCurrentCartOfUser(CartItemDTO cartItemDto, Long userId) throws StoreException {
		User user = userCatalog.findOne(userId);
		if (user == null) {
			throw new UserNotFoundException("User " + userId + " not found");
		}

		Cart currentCart = retrieveCurrentCartByUser(user);
		if (currentCart == null) {
			throw new NoCurrentCartException("No open cart for User " + user.getId() + " exists, please create one");
		}
		CartItem cartItem = createCartItemsFromDTO(cartItemDto);
		currentCart.mergeCartItem(cartItem);
		currentCart.updateTotal();
		currentCart.setModifiedAt(new Date());
		cartCatalog.save(currentCart);

		return currentCart;
	}

	/**
	 * Checkout the current cart of the usr
	 * 
	 * @param userId
	 *            the cart owner
	 * @return
	 * @throws StoreException 
	 */
	public Cart checkoutCurrentCartByUser(Long userId) throws StoreException {
		
		Cart currentCart = retrieveCurrentCartByUser(userId);
		return checkoutCart(currentCart.getId());

	}

	private Cart checkoutCart(Long cartId) {
		Cart cart = cartCatalog.findOne(cartId);
		cart.setStatus(CartStatus.ORDERED);
		cart.setModifiedAt(new Date());
		Order order = orderService.createOrderFromCart(cart);
		cartCatalog.save(cart);
		orderService.getOrderCatalog().save(order);
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
