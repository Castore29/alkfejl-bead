package edu.ik.alkfejl.fishingshop.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ORDERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {

	@Column(nullable = false)
	private long orderNumber;

	@ManyToOne(optional = false)
	private User user;

	@ManyToMany
	@JoinTable(name = "ORDERS_PRODUCTS")
	@JsonIgnore
	private List<Product> products;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	public enum Status {
		RECEIVED, PROCESSED, DELIVERING, CLOSED
	}

}
