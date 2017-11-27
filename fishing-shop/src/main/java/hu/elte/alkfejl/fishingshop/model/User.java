package hu.elte.alkfejl.fishingshop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The User class represents the users of the webshop. Extends from the
 * BaseEntity MappedSuperClass
 */

@Entity // User entity model stores the users' data in the database
@Table(name = "USERS") // Refers to the USERS table in the database
// Lombok annotation to generate getters and setters at runtime
@Data
// Lombok annotation to generate a constructor for the class with all data
// members as parameters at runtime
@AllArgsConstructor
// Lombok annotation to generate a constructor for the class without any
// parameters at runtime
@NoArgsConstructor
// Lombok annotation to generate equals and hashcode methods based on the
// superclass and the email property
@EqualsAndHashCode(callSuper = true, of = { "email" })
public class User extends BaseEntity {

	@NotEmpty // Hibernate annotation to validate that username is not empty or null
	@Column(nullable = false) // JPA annotation to restrict null value for username
	private String userName;

	@Email // Hibernate annotation to validate that email is a valid email address format
	@NotEmpty // Hibernate annotation to validate that email is not empty or null
	@Column(nullable = false, unique = true) // JPA annotation to restrict null value and ensure uniqueness for email
	private String email;

	@NotEmpty // Hibernate annotation to validate that password is not empty or null
	@Column(nullable = false) // JPA annotation to restrict null value for password
	private String password;

	private String address;

	private String phoneNumber;

	/**
	 * JPA annotation to mark a one-to-many relationship between a user and their
	 * orders. CascadeType.ALL makes sure that any change in the user is reflected
	 * in their orders. The mappedBy = "user" refers to the owner of the
	 * relationship, i.e. the user property of the Order class
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnore // Marker annotation to ignore the orders attribute during JSON serialization
	private Set<Order> orders = new HashSet<>();

	// JPA annotation for storing the role enum type as a string type in the
	// database
	@Enumerated(EnumType.STRING)
	@Column(nullable = false) // JPA annotation to restrict null value for role
	private Role role;

	// Role enum for basic authorization
	public enum Role {
		GUEST, USER, ADMIN
	}

}
