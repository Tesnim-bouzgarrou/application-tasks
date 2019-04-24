package de.tarent.challenge.store.orders;

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
import javax.persistence.Table;

import com.google.common.base.MoreObjects;

import de.tarent.challenge.store.users.User;

/**
 * @author tasnim
 *
 */
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUser", nullable = false)
	private User user;

	private Date createdAt;

	private Date modifiedAt;

	private OrderStatus status;

	@Column(name = "total", precision = 10)
	private BigDecimal total;

	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();

	private Order() {
	}

	public Order(User user, Set<OrderItem> items) {
		super();
		this.user = user;
		this.status = OrderStatus.CREATED;
		this.createdAt = new Date();

		for (OrderItem orderItem : items) {
			if (orderItem.getQuantity() > 0) {
				this.addOrderItem(orderItem);
							}
		}

		this.updateTotal();
	}

	public void updateTotal() {

		MathContext mc = new MathContext(4);
		BigDecimal total = new BigDecimal(0);
		for (OrderItem orderItem : orderItems) {
			BigDecimal price = orderItem.getProduct().getPrice();
			BigDecimal qte = BigDecimal.valueOf(orderItem.getQuantity());
			total = total.add(price.multiply(qte, mc));
		}
		this.total = total;

	}

	public Order(Set<OrderItem> items) {
		super();
		this.status = OrderStatus.CREATED;
		this.orderItems = items;
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
		 orderItem.setOrder(this);
	}

	public void removeOrderItem(OrderItem orderItem) {
		this.orderItems.remove(orderItem);
		orderItem.setOrder(null);
	}


	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("user", user.getId()).add("createdAt", this.createdAt)
				.add("status", this.status).toString();
	}

	
	

}
