package de.tarent.challenge.store.carts;

import static javax.persistence.GenerationType.AUTO;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.MoreObjects;

import de.tarent.challenge.store.products.Product;

@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_cart")
	private Cart cart;

	@ManyToOne
	@JoinColumn(name = "id_product")
	private Product product;

	private Long quantity;

	@Column(name = "price", nullable = false, precision = 10)
	private BigDecimal price;

	private CartItem() {
	}
	
	

	public CartItem(Product product, Long quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}



//	public Cart getCart() {
//		return cart;
//	}

	public CartItem(Cart cart, Product product, Long quantity, BigDecimal price) {
		super();
		this.cart = cart;
		this.product = product;
		this.quantity = quantity;
		this.price = price;
	}

	public CartItem(Product product, Long quantity, BigDecimal price) {
		this.product = product;
		this.quantity = quantity;
		this.price = price;
	}



	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CartItem cartItem = (CartItem) o;
		return Objects.equals(this.id, cartItem.id) &&  Objects.equals(this.cart, this.cart) &&  Objects.equals(this.product, cartItem.product) && Objects.equals(this.quantity, this.quantity)
				&& Objects.equals(this.price, cartItem.price);
	}

//	@Override
//	public int hashCode() {
//		return Objects.hash(id, cart.getId(), product.getId(), quantity, price);
//	}


	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("product", this.product.getName())
				.add("quantity", this.quantity).toString();
	}

}
