package de.tarent.challenge.store.products;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

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
				new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")));
		Product testProduct1 = new Product("Brot", "2035", new HashSet<>(Arrays.asList("34558821", "12323410")));
		Product testProduct2 = new Product("Käse", "S-155",
				new HashSet<>(Arrays.asList("34598146", "43565922", "23454045")));
		List<Product> products = new ArrayList<>();
		products.add(testProduct);
		products.add(testProduct1);
		products.add(testProduct2);
		
		
	  when(productCatalog.findAll()).thenReturn(products);
	  assertEquals(productService.retrieveAllProducts(), products);
	}
	
	
	
	@Test
	public void retrieveProductBySku() {
		Product testProduct = new Product("Milch", "102",
				new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")));
		
	  when(productCatalog.findBySku(anyString())).thenReturn(testProduct);
	  assertEquals(productService.retrieveProductBySku("102"), testProduct);
	}

	
	@Test(expected = NonUniqueResultException.class)
	public void givenProductExists_whenAddProduct_thenException() {
		Product testProduct = new Product("Milch", "102",
				new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")));
		
	  when(productCatalog.findBySku(anyString())).thenReturn(testProduct);
	  when(productCatalog.save(testProduct)).thenReturn(testProduct);
	  
	   productService.addProduct(testProduct);
	}
	
	@Test
	public void givenProductNotExists_whenAddProduct_thenOk() {
		Product testProduct = new Product("Milch", "102",
				new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")));
		
	  when(productCatalog.findBySku(anyString())).thenReturn(null);
	  when(productCatalog.save(testProduct)).thenReturn(testProduct);
	  
	  assertEquals(productService.addProduct(testProduct), testProduct);
	}
	
	@Test
	public void givenProductExists_whenUpdateProduct_thenOk() {
		Product testProduct = new Product("Milch", "102",
				new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")));
		
	  when(productCatalog.findBySku(anyString())).thenReturn(null);
	  when(productCatalog.save(testProduct)).thenReturn(testProduct);
	  
	  assertEquals(productService.updateProduct(testProduct), testProduct);
	}
	
	@Test
	public void givenProductNotExists_whenUpdateProduct_thenOk() {
		Product testProduct = new Product("Milch", "102",
				new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")));
		
	  when(productCatalog.findBySku(anyString())).thenReturn(null);
	  when(productCatalog.save(testProduct)).thenReturn(testProduct);
	  
	  assertEquals(productService.updateProduct(testProduct), testProduct);
	}

}
