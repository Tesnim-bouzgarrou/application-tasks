package de.tarent.challenge.store.products;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tarent.challenge.store.common.AbstractTest;
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
		Product testProduct = new Product("sku-test777", "name-test", new HashSet<>(Arrays.asList("34558821", "12323410")));
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

		String sku = given().contentType(ContentType.JSON).body(testProduct).when().post("/products/new").then()
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
		    .statusCode(HttpURLConnection.HTTP_OK);
	}
	
	
	  @Test
	  public void givenSkuNull_whenAddProduct_thenReturnsStatus400() throws Exception {
		  Product testProduct = new Product(null, "new-name-test", new HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")));
			
		  given()
		    .body(testProduct)
		    .contentType(ContentType.JSON)
		  .when()
		    .post("/products/new")
		  .then()
		    .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	  }
	
	  @Test
	  public void givenSkuEmpty_whenAddProduct_thenReturnsStatus400() throws Exception {
		  Product testProduct = new Product("", "new-name-test", new HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")));
			
		  given()
		    .body(testProduct)
		    .contentType(ContentType.JSON)
		  .when()
		    .post("/products/new")
		  .then()
		    .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	  }
	  
	  @Test
	  public void givenSkuExists_whenAddProduct_thenReturnsStatus400() throws Exception {
		  Product testProduct = new Product("345", "new-name-test", new HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")));
		  productService.addProduct(testProduct);
		  given()
		    .body(testProduct)
		    .contentType(ContentType.JSON)
		  .when()
		    .post("/products/new")
		  .then()
		    .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
		    
	  }
	  
	  @Test
	  public void givenNameNull_whenAddProduct_thenReturnsStatus400() throws Exception {
		  Product testProduct = new Product("abc", null, new HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")));
			
		  given()
		    .body(testProduct)
		    .contentType(ContentType.JSON)
		  .when()
		    .post("/products/new")
		  .then()
		    .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	  }
	
	  @Test
	  public void givenNameEmpty_whenAddProduct_thenReturnsStatus400() throws Exception {
		  Product testProduct = new Product("abc", "", new HashSet<>(Arrays.asList("eee1", "aaa1", "nnn1")));
			
		  given()
		    .body(testProduct)
		    .contentType(ContentType.JSON)
		  .when()
		    .post("/products/new")
		  .then()
		    .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	  }
	  
	  
	  @Test
	  public void givenEansEmpty_whenAddProduct_thenReturnsStatus400() throws Exception {
		  Product testProduct = new Product("abc", "product", new HashSet<>());
			
		  given()
		    .body(testProduct)
		    .contentType(ContentType.JSON)
		  .when()
		    .post("/products/new")
		  .then()
		    .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	  }
	  
	  @Test
	  public void givenEansWithEmptyElt_whenAddProduct_thenReturnsStatus400() throws Exception {
		  Product testProduct = new Product("abc", "product", new HashSet<>(Arrays.asList("eee1", "", "nnn1")));
			
		  given()
		    .body(testProduct)
		    .contentType(ContentType.JSON)
		  .when()
		    .post("/products/new")
		  .then()
		    .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
	  }
}
