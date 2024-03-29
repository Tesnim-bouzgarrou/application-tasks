package de.tarent.challenge.store.carts;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.NonUniqueResultException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.tarent.challenge.store.carts.dto.CartItemDTO;
import de.tarent.challenge.store.exceptions.StoreException;

@RestController
public class CartController {

	private final CartService cartService;

	public CartController(CartService productService) {
		this.cartService = productService;
	}

//	@GetMapping("/carts")
//	public Iterable<Cart> retrieveAllCarts() {
//		return cartService.retrieveAllCarts();
//	}
	

	/**
	 * Create a new Cart for User if it does not already exist.
	 */
	@PostMapping("cart/{userId}/new")
	public ResponseEntity<Object> createNewCart(@PathVariable Long userId,@Valid @RequestBody Set<CartItemDTO> items) throws StoreException {
		
		Cart createdCart = cartService.createNewCartForUser(userId,items);
		
		return new ResponseEntity<>(createdCart, HttpStatus.OK);

	}
	
	/**
	 * Check the current Cart for the user.
	 */
	@GetMapping("cart/{userId}")
	public ResponseEntity<Object> getCurrentCart(@PathVariable Long userId) throws StoreException {
		
		Cart currentCart = cartService.retrieveCurrentCartByUser(userId);
		return new ResponseEntity<>(currentCart, HttpStatus.OK);

	}
	
	/**
	 * Check cart by id and userId
	 */
	@GetMapping("cart/{userId}/{cartId}")  
	public ResponseEntity<Object> getCart(@PathVariable Long userId, @PathVariable Long cartId) throws StoreException {
		
		Cart currentCart = cartService.retrieveCartByIdAndUser(cartId , userId);
		return new ResponseEntity<>(currentCart, HttpStatus.OK);

	}
	
	
	/**
	 * Add new Item for the current (open) Cart of user.
	 */
	@PatchMapping("/cart/{userId}/addItem")
	public ResponseEntity<Object> addItemToCart(@PathVariable Long userId,@Valid  @RequestBody CartItemDTO cartItem) throws StoreException {
		
		Cart modifiedCart = cartService.addItemToCurrentCartOfUser(cartItem, userId);
		return new ResponseEntity<>(modifiedCart, HttpStatus.OK);

	}
	
	/**
	 * Checkout current cart of user
	 */
	@GetMapping("/cart/{userId}/checkout")
	public ResponseEntity<Object> checkoutCart(@PathVariable Long userId) throws StoreException {
		Cart checkedoutCart = cartService.checkoutCurrentCartByUser(userId);
		return new ResponseEntity<>(checkedoutCart, HttpStatus.OK);
	}
	
	

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NonUniqueResultException.class)
	public Map<String, String> handleNonUniqueExceptions(NonUniqueResultException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("", ex.getMessage());
		return errors;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(StoreException.class)
	public Map<String, String> handleStoreExceptions(StoreException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(ex.getClass().getName(), ex.getMessage());
		return errors;
	}

}
