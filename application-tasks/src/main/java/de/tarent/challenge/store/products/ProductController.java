package de.tarent.challenge.store.products;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public Iterable<Product> retrieveProducts() {
		return productService.retrieveAllProducts();
	}

	@GetMapping("/{sku}")
	public ResponseEntity<Object> retrieveProductBySku(@PathVariable String sku) {
		Product found = productService.retrieveProductBySku(sku);
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

	}

	
}
