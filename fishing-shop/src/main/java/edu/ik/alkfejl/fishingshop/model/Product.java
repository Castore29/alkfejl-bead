package edu.ik.alkfejl.fishingshop.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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

	@NotEmpty
	@Column(nullable = false)
	private long itemNumber;

	@NotEmpty
	@Column(nullable = false, unique = true)
	private String productName;

	@NotEmpty
	@Column(nullable = false)
	private String description;

	private String producer;

	@Column(nullable = false)
	private int stock;

	@Column(nullable = false)
	private boolean available = true;

	@Column(nullable = false)
	private int price;

	private int discount = 0;

	@ManyToMany(mappedBy = "products")
	@JsonIgnore
	private List<Order> orders;

	@ManyToMany
	@JoinTable(name = "PRODUCTS_TAGS")
	private Set<Tag> tags;

}
