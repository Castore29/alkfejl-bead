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

@Entity
@Table(name = "PRODUCTS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String itemNumber;

	private String category;

	private String subCategory;

	@NotEmpty
	@Column(nullable = false)
	private String productName;

	@NotEmpty
	@Column(nullable = false)
	private String description;

	private String producer;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] image;

	@Min(0)
	@Column(nullable = false)
	private Integer stock;

	@Column(nullable = false)
	private Boolean available = true;

	@Min(0)
	@Column(nullable = false)
	private Integer price;

	@Min(0)
	@Max(100)
	private Integer discount;

	@ManyToMany(mappedBy = "products")
	@JsonIgnore
	private List<Order> orders = new ArrayList<>();

}
