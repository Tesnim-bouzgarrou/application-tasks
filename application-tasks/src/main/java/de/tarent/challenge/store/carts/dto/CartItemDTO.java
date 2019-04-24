package de.tarent.challenge.store.carts.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class CartItemDTO {
	
	@NotEmpty
	private String sku;
	
	@NotNull
	@Min(0)
	private Long quantity;
	
	

	private CartItemDTO() {
		
	}

	public CartItemDTO(String sku, Long quantity) {
		
		this.sku = sku;
		this.quantity = quantity;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	

}
