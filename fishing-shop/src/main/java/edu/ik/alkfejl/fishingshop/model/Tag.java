package edu.ik.alkfejl.fishingshop.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TAGS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "tagName" })
public class Tag {

	@Id
	@Column(nullable = false, unique = true)
	private String tagName;

	@ManyToMany(mappedBy = "tags")
	@JsonIgnore
	private Set<Product> products;

}
