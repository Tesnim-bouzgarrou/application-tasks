package de.tarent.challenge.store.carts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import de.tarent.challenge.store.carts.dto.CartItemDTO;
import de.tarent.challenge.store.common.AbstractTest;
import de.tarent.challenge.store.exceptions.CartAlreadyExistForUser;
import de.tarent.challenge.store.exceptions.StoreException;
import de.tarent.challenge.store.exceptions.UserNotFoundException;
import de.tarent.challenge.store.products.Product;
import de.tarent.challenge.store.products.ProductCatalog;
import de.tarent.challenge.store.products.ProductService;
import de.tarent.challenge.store.users.User;
import de.tarent.challenge.store.users.UserCatalog;

/**
 * Unit Tests für Service Layer
 * @author tasnim
 *
 */
public class CartServiceTests extends AbstractTest {
	
	private CartService cartService;
	
	private final Product TEST_PRODUCT = new Product("Prod1", "222",
			new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")), BigDecimal.valueOf(20));
	private final Product TEST_PRODUCT1 = new Product("Prod2", "223",
			new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")), BigDecimal.valueOf(20));
	private final CartItem TEST_CART_ITEM = new CartItem(TEST_PRODUCT, 3L);
	private final CartItem TEST_CART_ITEM1 = new CartItem(TEST_PRODUCT1, 6L);
	private final Set<CartItem> TEST_CART_ITEMS = new HashSet<>(Arrays.asList(TEST_CART_ITEM,TEST_CART_ITEM1));
	private final User TEST_USER = new User("name");
	private final Cart TEST_CART = new Cart(TEST_USER,TEST_CART_ITEMS);
	private final User TEST_USER1 = new User("name1");
	private final Cart TEST_CART1 = new Cart(TEST_USER1,TEST_CART_ITEMS);
	
	private final CartItemDTO TEST_CART_ITEM_DTO = new CartItemDTO("102", 3L);
	private final CartItemDTO TEST_CART_ITEM1_DTO = new CartItemDTO("2035", 6L);
	private final Set<CartItemDTO> TEST_CART_ITEMS_DTO = new HashSet<>(Arrays.asList(TEST_CART_ITEM_DTO,TEST_CART_ITEM1_DTO));
	
	  @Mock
	  private CartCatalog cartCatalog;
	  
	  @Mock
	  private UserCatalog userCatalog;
	  
	  @Mock
	  private ProductCatalog productCatalog;
	  
	  @Autowired
	  private ProductService productService;

	
	@Before
	public void setup() {
		super.setUp();
		cartService = new CartService(cartCatalog);
		cartService.setUserCatalog(userCatalog);
		cartService.setProductCatalog(productCatalog);

	}
	
	@Before
	public void initDB() throws StoreException {
//		cartService.deleteAllCarts();
//		productService.deleteAllProducts();
//		productService.addProduct(TEST_PRODUCT);
//		productService.addProduct(TEST_PRODUCT1);

	}
	
	
	
	@Test
	public void testRetrieveAllCarts() {
		
		List<Cart> carts = new ArrayList<>();
		carts.add(TEST_CART);
		carts.add(TEST_CART1);
		
		
	  when(cartCatalog.findAll()).thenReturn(carts);
	  assertEquals(cartService.retrieveAllCarts(), carts);
	}
	
	
	@Test
	public void testRetrieveCurrentCartByUser_thenOk() throws StoreException {
	  when(cartCatalog.findByUserAndStatus(TEST_USER, CartStatus.CREATED)).thenReturn(TEST_CART);
	  assertEquals(cartService.retrieveCurrentCartByUser(TEST_USER), TEST_CART);
	}
	
	
	@Test
	public void givenUserExistWithoutCart_WhenCreateNewCartForUser_thenOk() throws StoreException {
	  when(userCatalog.findOne(anyLong())).thenReturn(TEST_USER);
	  when(cartService.retrieveCurrentCartByUser(TEST_USER)).thenReturn(null);
	  when(productCatalog.findBySku(anyString())).thenReturn(TEST_PRODUCT);
	
	  assertNotNull(cartService.createNewCartForUser(TEST_USER.getId(),TEST_CART_ITEMS_DTO));
	}
	
	
	
	@Test(expected = UserNotFoundException.class)
	public void givenUserNotExist_WhenCreateNewCartForUser_thenOk() throws StoreException {
	  when(userCatalog.findOne(anyLong())).thenReturn(null);
	  when(cartService.retrieveCurrentCartByUser(TEST_USER)).thenReturn(TEST_CART);
	
	  cartService.createNewCartForUser(34L,TEST_CART_ITEMS_DTO);
	}
	
	@Test(expected = CartAlreadyExistForUser.class)
	public void givenUserExistWithCart_WhenCreateNewCartForUser_thenExcpetion() throws StoreException {
	  when(userCatalog.findOne(anyLong())).thenReturn(TEST_USER);
	  when(cartService.retrieveCurrentCartByUser(TEST_USER)).thenReturn(TEST_CART);
	
	  cartService.createNewCartForUser(TEST_USER.getId(),TEST_CART_ITEMS_DTO);
	}
	
	


}
