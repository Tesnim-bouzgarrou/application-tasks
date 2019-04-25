package de.tarent.challenge.store.products;

import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.springframework.stereotype.Service;

import de.tarent.challenge.store.exceptions.NoProductWithSkufoundException;
import de.tarent.challenge.store.exceptions.StoreException;

@Service
public class ProductService {

	private final ProductCatalog productCatalog;

	public ProductService(ProductCatalog productCatalog) {
		this.productCatalog = productCatalog;
	}

	public List<Product> retrieveAllProducts() {
		return productCatalog.findAll();
	}

	public Product retrieveProductBySku(String sku) {
		return productCatalog.findBySku(sku);
	}

	public Product addProduct(Product product)throws NonUniqueResultException {

		Product existingProduct = retrieveProductBySku(product.getSku());
		if (existingProduct != null) {
			throw new NonUniqueResultException("Product sku is not unique");
		}
			
		
		return productCatalog.save(product);
	}
	
	public Product updateProduct(Product product) {

		Product existingProduct = retrieveProductBySku(product.getSku());
		if (existingProduct != null) {
			existingProduct.mergeWith(product);
			productCatalog.save(existingProduct);
		}
		return productCatalog.save(product);
	}

	public void deleteAllProducts() {
		//productCatalog.deleteAll();

	}

	public Product enableProductBySku(String sku) throws StoreException  {
		Product product = retrieveProductBySku(sku);
		if(product == null) {
			throw new NoProductWithSkufoundException(" No product with sku "+sku+" is found");
		}
		product.setEnabled(true);
		return productCatalog.save(product);
	}
	
	public Product disableProductBySku(String sku) throws StoreException {
		Product product = retrieveProductBySku(sku);
		if(product == null) {
			throw new NoProductWithSkufoundException(" No product with sku "+sku+" is found");
		}
		product.setEnabled(false);
		return productCatalog.save(product);
	}

}
