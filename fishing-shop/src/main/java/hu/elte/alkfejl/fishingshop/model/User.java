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

@Entity
@Table(name = "USERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

	@NotEmpty
	@Column(nullable = false)
	private String userName;

	@Email
	@NotEmpty
	@Column(nullable = false, unique = true)
	private String email;

	@NotEmpty
	@Column(nullable = false)
	private String password;

	private String address;

	private String phoneNumber;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnore
	private Set<Order> orders = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	public enum Role {
		GUEST, USER, ADMIN
	}

}
