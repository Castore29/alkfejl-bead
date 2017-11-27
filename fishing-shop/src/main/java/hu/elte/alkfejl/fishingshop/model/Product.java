package hu.elte.alkfejl.fishingshop.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
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

/**
 * The Product class represents the products sold by the webshop. Extends from
 * the BaseEntity MappedSuperClass
 */

@Entity // Product entity model stores the products' data in the database
@Table(name = "PRODUCTS") // Refers to the PRODUCTS table in the database
@Data // Lombok annotation to generate getters and setters at runtime
// Lombok annotation to generate a constructor for the class with all data
// members as parameters at runtime
@AllArgsConstructor
// Lombok annotation to generate a constructor for the class without any
// parameters at runtime
@NoArgsConstructor
// Lombok annotation to generate equals and hashcode methods based on the
// superclass and the itemNumber property
@EqualsAndHashCode(callSuper = true, of = { "itemNumber" })
public class Product extends BaseEntity {

	// JPA annotation to restrict null value and ensure uniqueness for itemNumber
	// itemNumber generation is the task of the admin
	@Column(nullable = false, unique = true)
	private String itemNumber;

	private String category;

	private String subCategory;

	@NotEmpty
	@Column(nullable = false) // JPA annotation to restrict null value for productName
	private String productName;

	@NotEmpty
	@Column(nullable = false) // JPA annotation to restrict null value for productName
	private String description;

	private String producer;

	/**
	 * JPA annotation to mark image as a Large Object. An array of bytes, which
	 * represents a binary file without its' name or extension. Currently accepts
	 * JPEG only (validated by the ProductService)
	 */
	@Lob
	/**
	 * JPA annotation to mark the column type as Basic for the primitive byte array.
	 * FetchType.LAZY defines that image property should be lazily loaded from the
	 * database, i.e. only when needed
	 */
	@Basic(fetch = FetchType.LAZY)
	private byte[] image;

	@Min(0) // JPA annotation to validate that the stock is not less than 0
	@Column(nullable = false) // JPA annotation to restrict null value for productName
	private Integer stock;

	@Column(nullable = false) // JPA annotation to restrict null value for available
	private Boolean available = true;

	@Min(0) // JPA annotation to validate that the stock is not less than 0
	@Column(nullable = false) // JPA annotation to restrict null value for price
	private Integer price;

	@Min(0) // JPA annotation to validate that the discount is not less than 0
	@Max(100) // JPA annotation to validate that the discount is not more than 100
	private Integer discount; // meant to be a percentage (0-100)

	/**
	 * JPA annotation to mark a many-to-many relationship between products and the
	 * orders in which they got ordered. The mappedBy = "products" refers to the
	 * owner of the relationship, i.e. the products property of the Order class. It
	 * is a List (not a Set) because ordering the same product in the same order
	 * makes it so the product object is added multiple times to the order object
	 */
	@ManyToMany(mappedBy = "products")
	@JsonIgnore // Marker annotation to ignore the orders attribute during JSON serialization
	private List<Order> orders = new ArrayList<>();

}
