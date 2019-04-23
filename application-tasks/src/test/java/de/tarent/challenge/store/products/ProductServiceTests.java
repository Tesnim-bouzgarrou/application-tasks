package de.tarent.challenge.store.products;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import de.tarent.challenge.store.common.AbstractTest;

/**
 * Unit Tests für Service Layer
 * @author tasnim
 *
 */
public class ProductServiceTests extends AbstractTest {
	
	private ProductService productService;
	
	private final Product TEST_PRODUCT = new Product("Milch", "102",
			new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")), BigDecimal.valueOf(20));
	
	  @Mock
	  private ProductCatalog productCatalog;

	
	@Before
	public void setup() {
		super.setUp();
		productService = new ProductService(productCatalog);

	}
	
	
	
	@Test
	public void retrieveAllProducts() {
		Product testProduct = new Product("Milch", "102",
				new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")), BigDecimal.valueOf(20));
		Product testProduct1 = new Product("Brot", "2035", new HashSet<>(Arrays.asList("34558821", "12323410")), BigDecimal.valueOf(30));
		Product testProduct2 = new Product("Käse", "S-155",
				new HashSet<>(Arrays.asList("34598146", "43565922", "23454045")), BigDecimal.valueOf(40));
		List<Product> products = new ArrayList<>();
		products.add(testProduct);
		products.add(testProduct1);
		products.add(testProduct2);
		
		
	  when(productCatalog.findAll()).thenReturn(products);
	  assertEquals(productService.retrieveAllProducts(), products);
	}
	
	
	
	@Test
	public void retrieveProductBySku() {
		
	  when(productCatalog.findBySku(anyString())).thenReturn(TEST_PRODUCT);
	  assertEquals(productService.retrieveProductBySku("102"), TEST_PRODUCT);
	}

	
	@Test(expected = NonUniqueResultException.class)
	public void givenProductExists_whenAddProduct_thenException() {
		
	  when(productCatalog.findBySku(anyString())).thenReturn(TEST_PRODUCT);
	  when(productCatalog.save(TEST_PRODUCT)).thenReturn(TEST_PRODUCT);
	  
	   productService.addProduct(TEST_PRODUCT);
	}
	
	@Test
	public void givenProductNotExists_whenAddProduct_thenOk() {
	
	  when(productCatalog.findBySku(anyString())).thenReturn(null);
	  when(productCatalog.save(TEST_PRODUCT)).thenReturn(TEST_PRODUCT);
	  
	  assertEquals(productService.addProduct(TEST_PRODUCT), TEST_PRODUCT);
	}
	
	@Test
	public void givenProductExists_whenUpdateProduct_thenOk() {
		
		
	  when(productCatalog.findBySku(anyString())).thenReturn(null);
	  when(productCatalog.save(TEST_PRODUCT)).thenReturn(TEST_PRODUCT);
	  
	  assertEquals(productService.updateProduct(TEST_PRODUCT), TEST_PRODUCT);
	}
	
	@Test
	public void givenProductNotExists_whenUpdateProduct_thenOk() {
		
	  when(productCatalog.findBySku(anyString())).thenReturn(null);
	  when(productCatalog.save(TEST_PRODUCT)).thenReturn(TEST_PRODUCT);
	  
	  assertEquals(productService.updateProduct(TEST_PRODUCT), TEST_PRODUCT);
	}

}
