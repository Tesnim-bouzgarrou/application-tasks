package de.tarent.challenge.store.carts;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tarent.challenge.store.carts.dto.CartItemDTO;
import de.tarent.challenge.store.common.AbstractTest;
import de.tarent.challenge.store.exceptions.CartAlreadyExistForUser;
import de.tarent.challenge.store.exceptions.CartWithoutItemsException;
import de.tarent.challenge.store.exceptions.NoCurrentCartException;
import de.tarent.challenge.store.exceptions.StoreException;
import de.tarent.challenge.store.exceptions.UserNotFoundException;
import de.tarent.challenge.store.products.Product;
import de.tarent.challenge.store.products.ProductService;
import io.restassured.http.ContentType;

/**
 * Functional (End to End) Tests for CartController
 * 
 * @author tasnim
 *
 */
public class CartControllerTests extends AbstractTest {

	@Autowired
	CartService cartService;

	@Autowired
	ProductService productService;

	private final CartItemDTO TEST_CART_ITEM_DTO = new CartItemDTO("102", 3L);
	private final CartItemDTO TEST_CART_ITEM1_DTO = new CartItemDTO("2035", 6L);
	private final CartItemDTO TEST_CART_ITEM2_DTO = new CartItemDTO("S-155", 1L);
	private final Set<CartItemDTO> TEST_CART_ITEMS_DTO = new HashSet<>(
			Arrays.asList(TEST_CART_ITEM_DTO, TEST_CART_ITEM1_DTO));

	@Test
	public void contextLoads() {
	}

	@Before
	public void setup() {
		super.setUp();

	}

	@Before
	public void initDB() throws StoreException {
		cartService.deleteAllCarts();
		// productService.deleteAllProducts();
		// productService.addProduct(TEST_PRODUCT);
		// productService.addProduct(TEST_PRODUCT1);

	}

	/************************************************
	 * RESt end points Tests
	 ********************************************/

//	@Test
//	public void testRetrieveAllCarts() throws Exception {
//
//		cartService.createNewCartForUser(1L, TEST_CART_ITEMS_DTO);
//
//		given().when().get("/carts").then().statusCode(HttpURLConnection.HTTP_OK).assertThat().body("", hasItems());
//
//		Cart[] carts = given().when().get("/carts").then().statusCode(200).extract().as(Cart[].class);
//		assertTrue(carts.length > 0);
//	}

	@Test
	public void givenCurrentCart_whenGetCurrentCart_thenReturnIt() throws Exception {

		cartService.createNewCartForUser(1L, TEST_CART_ITEMS_DTO);

		Cart cart = given().when().get("/cart/1").then().statusCode(HttpURLConnection.HTTP_OK).extract().as(Cart.class);
		assertNotNull(cart);

	}

	@Test
	public void givenNoCurrentCart_whenGetCurrentCart_thenException() throws Exception {

		given().when().get("/cart/1").then().statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
				.body(containsString(NoCurrentCartException.class.getName()));

	}

	@Test
	public void givenNoItems_whenCreateNewCart_thenError() throws Exception {

		Set<CartItem> items = new HashSet<>();
		given().contentType(ContentType.JSON).body(items).when().post("/cart/1/new").then().assertThat()
				.statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
				.body(containsString(CartWithoutItemsException.class.getName()));
		;

	}

	@Test
	public void givenItems_whenCreateNewCart_thenOk() throws Exception {

		given().contentType(ContentType.JSON).body(TEST_CART_ITEMS_DTO).when().post("/cart/1/new").then().assertThat()
				.statusCode(HttpURLConnection.HTTP_OK).extract().path("status").equals(CartStatus.CREATED.toString());

	}

	@Test
	public void givenCurrentCart_whenCreateNewCart_thenException() throws Exception {

		cartService.createNewCartForUser(1L, TEST_CART_ITEMS_DTO);
		given().contentType(ContentType.JSON).body(TEST_CART_ITEMS_DTO).when().post("/cart/1/new").then().assertThat()
				.statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
				.body(containsString(CartAlreadyExistForUser.class.getName()));
		;

	}

	@Test
	public void givenNewProduct_WennAddItemToCart_thenAddNewItem() throws Exception {

		Cart currentCart = cartService.createNewCartForUser(1L, TEST_CART_ITEMS_DTO);
		int numItms = currentCart.getCartItems().size();

		Cart cart = given().contentType(ContentType.JSON).body(TEST_CART_ITEM2_DTO).when().patch("/cart/1/addItem")
				.then().assertThat().statusCode(HttpURLConnection.HTTP_OK).extract().as(Cart.class);

		assertTrue(cart.getCartItems().size() == (numItms + 1));

	}

	@Test
	public void givenExistantProduct_WennAddItemToCart_thenUpdateQuantityItem() throws Exception {

		CartItemDTO item = new CartItemDTO("2035", 3L);
		Cart currentCart = cartService.createNewCartForUser(1L, TEST_CART_ITEMS_DTO);
		int numItms = currentCart.getCartItems().size();

		Cart cart = given().contentType(ContentType.JSON).body(item).when().patch("/cart/1/addItem").then().assertThat()
				.statusCode(HttpURLConnection.HTTP_OK).extract().as(Cart.class);

		assertTrue(cart.getCartItems().size() == numItms);
		assertTrue(cart.getCartItemOfProduct("2035").getQuantity() == 3);
	}

	@Test
	public void givenExistantProduct_WennAddItemToCartWithZeroQuantity_thenRemoveItem() throws Exception {

		CartItemDTO item = new CartItemDTO("2035", 0L);
		Cart currentCart = cartService.createNewCartForUser(1L, TEST_CART_ITEMS_DTO);
		int numItms = currentCart.getCartItems().size();

		Cart cart = given().contentType(ContentType.JSON).body(item).when().patch("/cart/1/addItem").then().assertThat()
				.statusCode(HttpURLConnection.HTTP_OK).extract().as(Cart.class);

		assertTrue(cart.getCartItems().size() == numItms - 1);
		assertNull(cart.getCartItemOfProduct("2035"));
	}

	@Test
	public void givenCurrentCart_whenCheckoutCart_thenCheckout() throws Exception {

		cartService.createNewCartForUser(1L, TEST_CART_ITEMS_DTO);

		Cart cart = given().when().get("/cart/1/checkout").then().statusCode(HttpURLConnection.HTTP_OK).extract()
				.as(Cart.class);
		assertNotNull(cart);
		assertTrue(cart.getStatus().equals(CartStatus.ORDERED));

	}

	@Test
	public void givenNoCurrentCart_whenCheckoutCart_thenException() throws Exception {

		given().when().get("/cart/1/checkout").then().statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
				.body(containsString(NoCurrentCartException.class.getName()));

	}
	
	@Test
	public void givenNoUser_whenCheckoutCart_thenException() throws Exception {

		given().when().get("/cart/3244/checkout").then().statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
				.body(containsString(UserNotFoundException.class.getName()));

	}

}
