package de.tarent.challenge.store.carts;

import static javax.persistence.GenerationType.AUTO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;

import de.tarent.challenge.store.users.User;

/**
 * @author tasnim
 *
 */
@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUser", nullable = false)
	private User user;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date modifiedAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date checkedOutAt;

	private CartStatus status;

	@Column(name = "total", precision = 10)
	private BigDecimal total;

	@OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<CartItem> cartItems = new HashSet<CartItem>();

	private Cart() {
	}

	public Cart(User user, Set<CartItem> items) {
		super();
		this.user = user;
		this.status = CartStatus.CREATED;
		this.createdAt = new Date();

		for (CartItem cartItem : items) {
			if (cartItem.getQuantity() > 0) {
				this.addCartItem(cartItem);

			}
		}

		this.updateTotal();
	}

	public synchronized void updateTotal() {

		MathContext mc = new MathContext(4);
		BigDecimal total = new BigDecimal(0);
		for (CartItem cartItem : cartItems) {
			BigDecimal price = cartItem.getProduct().getPrice();
			BigDecimal qte = BigDecimal.valueOf(cartItem.getQuantity());
			total = total.add(price.multiply(qte, mc));
		}
		this.total = total;

	}

	public Cart(Set<CartItem> items) {
		super();
		this.status = CartStatus.CREATED;
		this.cartItems = items;
		this.createdAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public CartStatus getStatus() {
		return status;
	}

	public void setStatus(CartStatus status) {
		this.status = status;
	}

	public Set<CartItem> getCartItems() {
		// return Sets.newHashSet(cartItems);
		return cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public void addCartItem(CartItem cartItem) {
		this.cartItems.add(cartItem);
		cartItem.setCart(this);
	}

	public void removeCartItem(CartItem cartItem) {
		this.cartItems.remove(cartItem);
		cartItem.setCart(null);
	}

	public Date getCheckedOutAt() {
		return checkedOutAt;
	}

	public void setCheckedOutAt(Date checkedOutAt) {
		this.checkedOutAt = checkedOutAt;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("user", user.getId()).add("createdAt", this.createdAt)
				.add("status", this.status).toString();
	}

	/**
	 * If the product exists already in the cart just update quantity and price If
	 * the product exists already in the cart and the quantity is zero then remove
	 * the CartItem If the product is new, add it as a CartItem
	 * 
	 * @param cartItem
	 */
	public synchronized void mergeCartItem(CartItem cartItem) {
		CartItem existingProductcartItem = this.cartItems.stream()
				.filter(item -> item.getProduct().getSku().equals(cartItem.getProduct().getSku())).findAny()
				.orElse(null);
		if (existingProductcartItem != null) {
			if (cartItem.getQuantity() > 0) {
				existingProductcartItem.setQuantity(cartItem.getQuantity());
				existingProductcartItem.setPrice(cartItem.getProduct().getPrice());
			} else {
				this.removeCartItem(existingProductcartItem);
			}
		} else {
			this.addCartItem(cartItem);
		}

	}

	public CartItem getCartItemOfProduct(String productSku) {
		CartItem existingProductcartItem = this.cartItems.stream()
				.filter(item -> item.getProduct().getSku().equals(productSku)).findAny().orElse(null);
		return existingProductcartItem;
	}

}
