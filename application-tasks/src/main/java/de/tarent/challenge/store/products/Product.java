package de.tarent.challenge.store.products;

import static javax.persistence.GenerationType.AUTO;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

import de.tarent.challenge.store.validator.NotEmptyElements;
import de.tarent.challenge.store.validator.NotEmptyElementsValidator;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(unique = true)
    @NotEmpty
    private String sku;

    @NotEmpty
    private String name;

    @ElementCollection
    @NotEmpty
    @NotEmptyElements
    private Set<String> eans;

    private Product() {
    }

    public Product(String sku, String name, Set<String> eans) {
        this.sku = sku;
        this.name = name;
        this.eans = eans;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public Set<String> getEans() {
        return Sets.newHashSet(eans);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(sku, product.sku) &&
                Objects.equals(name, product.name) &&
                Objects.equals(eans, product.eans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, name, eans);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("sku", sku)
                .add("name", name)
                .add("eans", eans)
                .toString();
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setEans(Set<String> eans) {
		this.eans = eans;
	}

	public void mergeWith(Product product) {
		this.setName(product.getName());
		this.setEans(product.getEans());
		
	}
}
