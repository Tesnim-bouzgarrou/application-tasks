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

	private Date createdAt;

	private Date modifiedAt;

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
			this.addCartItem(cartItem);
			cartItem.setCart(this);
		}
		
		this.updateTotal();
	}
	
	public void updateTotal() {

		MathContext mc = new MathContext(4); 
		BigDecimal total = new BigDecimal(0);
		for (CartItem cartItem : cartItems) {
			BigDecimal price = cartItem.getProduct().getPrice();
			BigDecimal qte = BigDecimal.valueOf(cartItem.getQuantity());
			total = total.add(price.multiply(qte, mc));
		}
		this.total = total;
		
	}



	public Cart( Set<CartItem> items) {
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
		//return Sets.newHashSet(cartItems);
		return cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
	
	 public void addCartItem(CartItem cartItem) {
		 this.cartItems.add(cartItem);
		 //cartItem.setCart(this);
	    }
	 
	    public void removeCartItem(CartItem cartItem) {
	    	this.cartItems.remove(cartItem);
	        cartItem.setCart(null);
	    }

//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (o == null || getClass() != o.getClass())
//			return false;
//		Cart cart = (Cart) o;
//		return Objects.equals(this.id, cart.id) && Objects.equals(this.total, cart.total)
//				&& Objects.equals(this.createdAt, cart.createdAt) && Objects.equals(this.modifiedAt, cart.modifiedAt);
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(id, total, user.getId());
//	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("user", user.getId()).add("createdAt", this.createdAt).add("status", this.status)
				.toString();
	}

}
