package hu.elte.alkfejl.fishingshop.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The Order class represents the orders made by the users of the webshop.
 * Extends from the BaseEntity MappedSuperClass Contains the user's data and a
 * list of the products
 */

@Entity // Order entity model stores the orders' data in the database
@Table(name = "ORDERS") // Refers to the ORDERS table in the database
@Data // Lombok annotation to generate getters and setters at runtime
// Lombok annotation to generate a constructor for the class with all data
// members as parameters at runtime
@AllArgsConstructor
// Lombok annotation to generate a constructor for the class without any
// parameters at runtime
@NoArgsConstructor
// Lombok annotation to generate equals and hashcode methods based on the
// superclass
@EqualsAndHashCode(callSuper = true, of = { "orderNumber" })
public class Order extends BaseEntity {

	// JPA annotation to restrict null value and ensure uniqueness for orderNumber
	@Column(nullable = false, unique = true)
	private String orderNumber;

	/**
	 * JPA annotation to mark a many-to-one relationship between the orders and
	 * their corresponding user. optional = false means that every order must have a
	 * user attached to it, who made the order
	 */
	@ManyToOne(optional = false)
	private User user;

	@Column(nullable = false) // JPA annotation to restrict null value for total
	private Integer total;

	/**
	 * JPA annotation to mark a many-to-many relationship between an order and the
	 * products that got ordered in a single order. It is a List (not a Set) because
	 * ordering the same product in the same order makes it so the product object is
	 * added multiple times to the order object
	 */
	@ManyToMany
	/**
	 * JPA annotation to mark the name of the join table, which has the ids of the
	 * orders linked to the ids of the corresponding ordered products
	 */
	@JoinTable(name = "ORDERS_PRODUCTS")
	private List<Product> products = new ArrayList<>();

	// JPA annotation for storing the status enum type as a string type in the
	// database
	@Enumerated(EnumType.STRING)
	@Column(nullable = false) // JPA annotation to restrict null value for status
	private Status status;

	// Status enum for basic order status tracking
	public enum Status {
		RECEIVED, PROCESSED, DELIVERING, CLOSED, CANCELLED
	}

}
