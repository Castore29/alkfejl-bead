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

@Entity
@Table(name = "ORDERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {

	@Column(nullable = false)
	private String orderNumber;

	@ManyToOne(optional = false)
	private User user;
	
	@Column(nullable = false)
	private Integer total;

	@ManyToMany
	@JoinTable(name = "ORDERS_PRODUCTS")
	private List<Product> products = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	public enum Status {
		RECEIVED, PROCESSED, DELIVERING, CLOSED, CANCELLED
	}

}
