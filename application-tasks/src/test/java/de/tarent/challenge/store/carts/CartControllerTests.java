package de.tarent.challenge.store.carts;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertNotNull;
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
import de.tarent.challenge.store.products.Product;
import de.tarent.challenge.store.products.ProductService;
import de.tarent.challenge.store.users.User;
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

	private final Product TEST_PRODUCT = new Product("Prod1", "222",
			new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")), BigDecimal.valueOf(20));
	private final Product TEST_PRODUCT1 = new Product("Prod2", "223",
			new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")), BigDecimal.valueOf(20));
	private final CartItem TEST_CART_ITEM = new CartItem(TEST_PRODUCT, 3L);
	private final CartItem TEST_CART_ITEM1 = new CartItem(TEST_PRODUCT1, 6L);
	private final Set<CartItem> TEST_CART_ITEMS = new HashSet<>(Arrays.asList(TEST_CART_ITEM, TEST_CART_ITEM1));
	private final User TEST_USER = new User("name");
	private final Cart TEST_CART = new Cart(TEST_USER, TEST_CART_ITEMS);
	private final User TEST_USER1 = new User("name1");
	private final Cart TEST_CART1 = new Cart(TEST_USER1, TEST_CART_ITEMS);
	private final CartItemDTO TEST_CART_ITEM_DTO = new CartItemDTO("102", 3L);
	private final CartItemDTO TEST_CART_ITEM1_DTO = new CartItemDTO("2035", 6L);
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
		productService.deleteAllProducts();
		productService.addProduct(TEST_PRODUCT);
		productService.addProduct(TEST_PRODUCT1);

	}

	/************************************************
	 * RESt end points Tests
	 ********************************************/

	@Test
	public void testRetrieveAllCarts() throws Exception {
		
		cartService.createNewCartForUser(1L, TEST_CART_ITEMS_DTO);
		
		given().when().get("/carts").then().statusCode(HttpURLConnection.HTTP_OK).assertThat().body("", hasItems());

		Cart[] carts = given().when().get("/carts").then().statusCode(200).extract().as(Cart[].class);
		assertTrue(carts.length > 0);
	}

	@Test
	public void givenCurrentCart_whenGetCurrentCart_thenReturnIt() throws Exception {
		
		cartService.createNewCartForUser(1L, TEST_CART_ITEMS_DTO);
		
		Cart cart = given().when().get("/cart/1").then().statusCode(HttpURLConnection.HTTP_OK).extract().as(Cart.class);
		assertNotNull(cart);
	
	}

	@Test
	public void givenNoCurrentCart_whenGetCurrentCart_thenException() throws Exception {
		
		given().when().get("/cart/1").then().statusCode(HttpURLConnection.HTTP_BAD_REQUEST).body(containsString(NoCurrentCartException.class.getName()));
	
	}
	
	@Test
	public void givenNoItems_whenCreateNewCart_thenError() throws Exception {

		Set<CartItem> items = new HashSet<>();
		given().contentType(ContentType.JSON).body(items).when().post("/cart/1/new").then().assertThat()
				.statusCode(HttpURLConnection.HTTP_BAD_REQUEST).body(containsString(CartWithoutItemsException.class.getName()));;

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
				.statusCode(HttpURLConnection.HTTP_BAD_REQUEST).body(containsString(CartAlreadyExistForUser.class.getName()));;

	}

	// @Test
	// public void givenExistantSku_whenRetrieveProductBySku_thenReturnOk() throws
	// Exception {
	// Product testProduct = new Product("sku-test777", "name-test", new
	// HashSet<>(Arrays.asList("34558821", "12323410")), BigDecimal.valueOf(20));
	// cartService.addProduct(testProduct);
	//
	// get("/products/" +
	// testProduct.getSku()).then().assertThat().statusCode(HttpURLConnection.HTTP_OK)
	// .body("sku", equalTo(testProduct.getSku())).body("name",
	// equalTo(testProduct.getName()));
	//
	// }
	//
	// @Test
	// public void givenNonexistantSku_whenRetrieveProductBySku_thenReturnNotFound()
	// throws Exception {
	//
	// String sku = RandomStringUtils.randomAlphabetic(8);
	// get("/products/" +
	// sku).then().assertThat().statusCode(HttpURLConnection.HTTP_NOT_FOUND);
	//
	// }
	//
	//
	//
	// @Test
	// public void createProduct() throws Exception {
	// Product testProduct = new Product("sku-test777", "new-name-test",
	// new HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")),
	// BigDecimal.valueOf(20));
	//
	// String sku =
	// given().contentType(ContentType.JSON).body(testProduct).when().post("/products/new").then()
	// .assertThat().statusCode(HttpURLConnection.HTTP_CREATED).extract().path("sku");
	// assertEquals(sku, testProduct.getSku());
	//
	// }
	//
	// @Test
	// public void updateProduct() throws Exception {
	//
	// Product testProduct = new Product("sku-test777", "new-name-test", new
	// HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")), BigDecimal.valueOf(20));
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .put("/products/"+testProduct.getSku())
	// .then()
	// .statusCode(HttpURLConnection.HTTP_OK);
	// }
	//
	/************************************************
	 * Bean Validation Tests
	 ********************************************/

	// @Test
	// public void givenSkuNull_whenAddProduct_thenReturnsStatus400() throws
	// Exception {
	// Product testProduct = new Product(null, "new-name-test", new
	// HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")), BigDecimal.valueOf(20));
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	// }
	//
	// @Test
	// public void givenSkuEmpty_whenAddProduct_thenReturnsStatus400() throws
	// Exception {
	// Product testProduct = new Product("", "new-name-test", new
	// HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")), BigDecimal.valueOf(20));
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	// }
	//
	// @Test
	// public void givenSkuExists_whenAddProduct_thenReturnsStatus400() throws
	// Exception {
	// Product testProduct = new Product("345", "new-name-test", new
	// HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")), BigDecimal.valueOf(20));
	// cartService.addProduct(testProduct);
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	//
	// }
	//
	// @Test
	// public void givenNameNull_whenAddProduct_thenReturnsStatus400() throws
	// Exception {
	// Product testProduct = new Product("abc", null, new
	// HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")), BigDecimal.valueOf(20));
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	// }
	//
	// @Test
	// public void givenNameEmpty_whenAddProduct_thenReturnsStatus400() throws
	// Exception {
	// Product testProduct = new Product("abc", "", new
	// HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")), BigDecimal.valueOf(20));
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	// }
	//
	//
	// @Test
	// public void givenEansEmpty_whenAddProduct_thenReturnsStatus400() throws
	// Exception {
	// Product testProduct = new Product("abc", "product", new HashSet<>(),
	// BigDecimal.valueOf(20));
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	// }
	//
	// @Test
	// public void givenEansWithEmptyElt_whenAddProduct_thenReturnsStatus400()
	// throws Exception {
	// Product testProduct = new Product("abc", "product", new
	// HashSet<>(Arrays.asList("eee1", "", "nnn1")), BigDecimal.valueOf(20));
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	// }
	//
	//
	// @Test
	// public void givenPriceNull_whenAddProduct_thenReturnsStatus400() throws
	// Exception {
	// Product testProduct = new Product("abc", "nameProduct", new
	// HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")), null);
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	// }
	//
	// @Test
	// public void givenPriceZero_whenAddProduct_thenReturnsStatus400() throws
	// Exception {
	// Product testProduct = new Product("abc", "nameProduct", new
	// HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")), BigDecimal.valueOf(0));
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	// }
	//
	// @Test
	// public void givenPriceNegative_whenAddProduct_thenReturnsStatus400() throws
	// Exception {
	// Product testProduct = new Product("abc", "nameProduct", new
	// HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")),
	// BigDecimal.valueOf(-23.66));
	//
	// given()
	// .body(testProduct)
	// .contentType(ContentType.JSON)
	// .when()
	// .post("/products/new")
	// .then()
	// .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	// }
}
