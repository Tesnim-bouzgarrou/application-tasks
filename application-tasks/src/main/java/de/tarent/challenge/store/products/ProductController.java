package de.tarent.challenge.store.products;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.NonUniqueResultException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.tarent.challenge.store.exceptions.StoreException;

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

	@PostMapping("/new")
	public ResponseEntity<Object> createProduct(@Valid @RequestBody Product product) throws NonUniqueResultException {
		productService.addProduct(product);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	@PutMapping("/{sku}")
	public ResponseEntity<Object> updateProduct(@Valid @RequestBody Product product) {
		productService.updateProduct(product);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	
	@PutMapping("/{sku}/enable")
	public ResponseEntity<Object> enableProduct(@PathVariable String sku) throws StoreException {
		Product product = productService.enableProductBySku(sku);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	@PutMapping("/{sku}/disable")
	public ResponseEntity<Object> disableProduct(@PathVariable String sku) throws StoreException {
		Product product = productService.disableProductBySku(sku);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NonUniqueResultException.class)
	public Map<String, String> handleNonUniqueExceptions(NonUniqueResultException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("", ex.getMessage());
		return errors;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(StoreException.class)
	public Map<String, String> handleStoreExceptions(StoreException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(ex.getClass().getName(), ex.getMessage());
		return errors;
	}

}
