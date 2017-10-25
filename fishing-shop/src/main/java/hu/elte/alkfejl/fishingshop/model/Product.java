package hu.elte.alkfejl.fishingshop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCTS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

	@Column(nullable = false, unique = true)
	private long itemNumber;

	@NotEmpty
	@Column(nullable = false)
	private String productName;

	@NotEmpty
	@Column(nullable = false)
	private String description;

	private String producer;

	@Min(0)
	@Column(nullable = false)
	private int stock;

	@Column(nullable = false)
	private boolean available = true;

	@Min(0)
	@Column(nullable = false)
	private int price;

	@Min(0)
	@Max(100)
	private int discount;

	@ManyToMany(mappedBy = "products")
	@JsonIgnore
	private List<Order> orders = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "PRODUCTS_TAGS")
	private Set<Tag> tags = new HashSet<>();

}
