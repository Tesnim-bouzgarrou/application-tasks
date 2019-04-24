package de.tarent.challenge.store.carts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.tarent.challenge.store.products.Product;
import de.tarent.challenge.store.products.ProductCatalog;
import de.tarent.challenge.store.users.User;
import de.tarent.challenge.store.users.UserCatalog;

/**
 * Integration Tests for Cart Repository
 * 
 * @author tasnim
 *
 */

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class CartCatalogTests {

	@Autowired
	CartCatalog cartCatalog;
	
	@Autowired
	UserCatalog userCatalog;
	
	@Autowired
	ProductCatalog productCatalog;
	
	private final Product TEST_PRODUCT = new Product("Prod1", "222",
			new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")), BigDecimal.valueOf(20));
	private final Product TEST_PRODUCT1 = new Product("Prod2", "223",
			new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")), BigDecimal.valueOf(20));
	private final CartItem TEST_CART_ITEM = new CartItem(TEST_PRODUCT, 3L);
	private final CartItem TEST_CART_ITEM1 = new CartItem(TEST_PRODUCT1, 6L);
	private final Set<CartItem> TEST_CART_ITEMS = new HashSet<>(Arrays.asList(TEST_CART_ITEM,TEST_CART_ITEM1));

	
	
	@Before
	public void initDB() {
		
		
	}

	/**
	 * Tests inserting a cart and asserts it can be loaded again.
	 */
	@Test
	public void testSave() {
		productCatalog.save(TEST_PRODUCT);
		productCatalog.save(TEST_PRODUCT1);
		User user = userCatalog.findOne(1L);

		Cart cart = new Cart(user,TEST_CART_ITEMS);

		Cart persistedCart = cartCatalog.save(cart);
		assertEquals(cart, persistedCart);
	}
	
	/**
	 * Tests getting Cart for user with Status.
	 */
	@Test
	public void testFindByUserAndStatus() {
		User user = userCatalog.findOne(1L);

		
		Cart cart = new Cart(user,TEST_CART_ITEMS);
		for (CartItem cartItem : cart.getCartItems()) {
			cartItem.setPrice(cartItem.getProduct().getPrice());
		}
		cartCatalog.save(cart);
		

		Cart cart1 = cartCatalog.findByUserAndStatus(user, CartStatus.CREATED);
		assertNotNull(cart1);
	}


}
