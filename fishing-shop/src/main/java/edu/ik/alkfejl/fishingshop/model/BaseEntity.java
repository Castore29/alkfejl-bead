package edu.ik.alkfejl.fishingshop.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(of = { "id" })
public abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Version
	private long version;

	private Date createDate;

	private boolean active;

	@PrePersist
	void preInsert() {
		if (this.createDate == null) {
			this.createDate = new Date();
			this.active = true;
		}
	}

}
