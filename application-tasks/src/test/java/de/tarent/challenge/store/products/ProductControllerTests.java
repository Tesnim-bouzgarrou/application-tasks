package de.tarent.challenge.store.products;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.when;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import de.tarent.challenge.store.common.AbstractTest;
import de.tarent.challenge.store.products.Product;
import de.tarent.challenge.store.products.ProductService;
import io.restassured.http.ContentType;

public class ProductControllerTests extends AbstractTest {

	@Autowired
	ProductService productService;

	@Test
	public void contextLoads() {
	}

	@Before
	public void setup() {
		super.setUp();

	}

	@Before
	public void initDB() {
		productService.deleteAllProducts();
		Product testProduct = new Product("Milch", "102",
				new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")));
		Product testProduct1 = new Product("Brot", "2035", new HashSet<>(Arrays.asList("34558821", "12323410")));
		Product testProduct2 = new Product("Käse", "S-155",
				new HashSet<>(Arrays.asList("34598146", "43565922", "23454045")));
		productService.addProduct(testProduct);
		productService.addProduct(testProduct1);
		productService.addProduct(testProduct2);
	}

	@Test
	public void retrieveProducts() throws Exception {
		given().when().get("/products").then().statusCode(HttpURLConnection.HTTP_OK).assertThat().body("", hasItems());

		Product[] products = given().when().get("/products").then().statusCode(200).extract().as(Product[].class);
		assertTrue(products.length > 0);
	}

	@Test
	public void givenExistantSku_whenRetrieveProductBySku_thenReturnOk() throws Exception {
		Product testProduct = new Product("sku-test777", "name-test", new HashSet<>());
		productService.addProduct(testProduct);

		get("/products/" + testProduct.getSku()).then().assertThat().statusCode(HttpURLConnection.HTTP_OK)
				.body("sku", equalTo(testProduct.getSku())).body("name", equalTo(testProduct.getName()));

	}

	@Test
	public void givenNonexistantSku_whenRetrieveProductBySku_thenReturnNotFound() throws Exception {

		String sku = RandomStringUtils.randomAlphabetic(8);
		get("/products/" + sku).then().assertThat().statusCode(HttpURLConnection.HTTP_NOT_FOUND);

	}



	@Test
	public void createProduct() throws Exception {
		Product testProduct = new Product("sku-test777", "new-name-test",
				new HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")));

		String sku = given().contentType("application/json").body(testProduct).when().post("/products/new").then()
				.assertThat().statusCode(HttpURLConnection.HTTP_CREATED).extract().path("sku");
		assertEquals(sku, testProduct.getSku());

	}

	@Test
	public void updateProduct() throws Exception {
		
		Product testProduct = new Product("sku-test777", "new-name-test", new HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")));
		
		
		  given()
		    .body(testProduct)
		    .contentType(ContentType.JSON)
		  .when()
		    .put("/products/"+testProduct.getSku())
		  .then()
		    .statusCode(HttpStatus.SC_OK);
	}
	
}
