package de.tarent.challenge.store.products;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration Tests for Product Repository
 * 
 * @author tasnim
 *
 */

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class ProductCatalogTests {

	@Autowired
	ProductCatalog productCatalog;

	private final Product TEST_PRODUCT = new Product("Milch", "102",
			new HashSet<>(Arrays.asList("12345678", "77777777", "23498128")), BigDecimal.valueOf(20));

	/**
	 * Tests inserting a product and asserts it can be loaded again.
	 */
	@Test
	public void testSave() {

		Product testProduct = productCatalog.save(TEST_PRODUCT);

		Product persistedProduct = productCatalog.findOne(testProduct.getId());
		assertEquals(persistedProduct, testProduct);
	}

	/**
	 * Tests inserting a product with certin sku and asserts it can be loaded again
	 * by findBySku meth.
	 */
	@Test
	public void testFindBySku() {

		Product testProduct = productCatalog.save(TEST_PRODUCT);

		Product persistedProduct = productCatalog.findBySku(testProduct.getSku());
		assertEquals(persistedProduct.getSku(), testProduct.getSku());
	}
}
